package gg.rsmod.game.plugin

import gg.rsmod.game.model.COMMAND_ARGS_ATTR
import gg.rsmod.game.model.COMMAND_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.GameService
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner

/**
 * A repository that is responsible for storing and executing plugins, as well
 * as making sure no plugin collides.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginRepository {

    companion object {
        private val logger = LogManager.getLogger(PluginRepository::class.java)
    }

    /**
     * The total amount of plugins.
     */
    private var count = 0

    /**
     * A list of plugins that will be executed upon login.
     */
    private val loginPlugins = arrayListOf<Function1<Plugin, Unit>>()

    /**
     * A map that contains command plugins.
     */
    private val commandPlugins = hashMapOf<String, Function1<Plugin, Unit>>()

    /**
     * A map of button click plugins. The key is a shifted value of the parent
     * and child id.
     */
    private val buttonPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map that contains any plugin that will be executed upon entering a new
     * region. The key is the region id and the value is a list of plugins
     * that will execute upon entering the region.
     */
    private val enterRegionPlugins = hashMapOf<Int, MutableList<Function1<Plugin, Unit>>>()

    /**
     * A map that contains any plugin that will be executed upon leaving a region.
     * The key is the region id and the value is a list of plugins that will execute
     * upon leaving the region.
     */
    private val exitRegionPlugins = hashMapOf<Int, MutableList<Function1<Plugin, Unit>>>()

    /**
     * A map that contains objects and any associated menu-click and its respective
     * plugin executor, if any (would not be in the map if it doesn't have a plugin).
     */
    private val objectPlugins = hashMapOf<Int, HashMap<Int, Function1<Plugin, Unit>>>()

    /**
     * A map of objects that have custom path finding. This means that the plugin
     * is responsible for walking to the object if necessary.
     */
    private val customPathingObjects = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * Initiates and populates all our plugins.
     */
    fun init(gameService: GameService, packagePath: String) {
        scanForPlugins(packagePath)
        gameService.world.pluginExecutor.init(gameService)
    }

    /**
     * Goes through all the methods found in our [packagePath] system file path
     * and looks for any [ScanPlugins] annotation to invoke.
     *
     * @throws Exception If the plugin registration function could not be invoked.
     *                   Possible reasons: must be static [JvmStatic]
     */
    @Throws(Exception::class)
    fun scanForPlugins(packagePath: String) {
        loginPlugins.clear()
        commandPlugins.clear()
        buttonPlugins.clear()
        enterRegionPlugins.clear()
        exitRegionPlugins.clear()
        objectPlugins.clear()

        Reflections(packagePath, SubTypesScanner(false), MethodAnnotationsScanner()).getMethodsAnnotatedWith(ScanPlugins::class.java).forEach { method ->
            if (!method.declaringClass.name.contains("$") && !method.declaringClass.name.endsWith("Package")) {
                try {
                    method.invoke(null, this)
                } catch (e: Exception) {
                    logger.error("Error loading plugin: ${method.declaringClass} [$method]", e)
                    throw e
                }
            }
        }
    }

    /**
     * Get the total amount of plugins loaded from the plugins path.
     */
    fun getCount(): Int = count

    fun bindLogin(plugin: Function1<Plugin, Unit>) {
        loginPlugins.add(plugin)
        count++
    }

    fun executeLogin(p: Player) {
        loginPlugins.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    @Throws(IllegalStateException::class)
    fun bindCommand(command: String, plugin: Function1<Plugin, Unit>) {
        val cmd = command.toLowerCase()
        if (commandPlugins.containsKey(cmd)) {
            logger.error("Command is already bound to a plugin: $cmd")
            throw IllegalStateException()
        }
        commandPlugins[cmd] = plugin
        count++
    }

    fun executeCommand(p: Player, command: String, args: Array<String>? = null): Boolean {
        val plugin = commandPlugins[command]
        if (plugin != null) {
            p.attr.put(COMMAND_ATTR, command)
            if (args != null) {
                p.attr.put(COMMAND_ARGS_ATTR, args)
            } else {
                p.attr.put(COMMAND_ARGS_ATTR, emptyArray())
            }
            p.world.pluginExecutor.execute(p, plugin)
            return true
        }
        return false
    }

    @Throws(IllegalStateException::class)
    fun bindButton(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) {
        val hash = (parent shl 16) or child
        if (buttonPlugins.containsKey(hash)) {
            logger.error("Button hash already bound to a plugin: [parent=$parent, child=$child]")
            throw IllegalStateException()
        }
        buttonPlugins[hash] = plugin
        count++
    }

    fun executeButton(p: Player, parent: Int, child: Int): Boolean {
        val hash = (parent shl 16) or child
        val plugin = buttonPlugins[hash]
        if (plugin != null) {
            p.world.pluginExecutor.execute(p, plugin)
            return true
        }
        return false
    }

    fun bindRegionEnter(regionId: Int, plugin: Function1<Plugin, Unit>) {
        val plugins = enterRegionPlugins[regionId]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            enterRegionPlugins[regionId] = arrayListOf(plugin)
        }
        count++
    }

    fun executeRegionEnter(p: Player, regionId: Int) {
        enterRegionPlugins[regionId]?.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    fun bindRegionExit(regionId: Int, plugin: Function1<Plugin, Unit>) {
        val plugins = exitRegionPlugins[regionId]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            exitRegionPlugins[regionId] = arrayListOf(plugin)
        }
        count++
    }

    fun executeRegionExit(p: Player, regionId: Int) {
        exitRegionPlugins[regionId]?.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    @Throws(IllegalStateException::class)
    fun bindObject(id: Int, opt: Int, plugin: Function1<Plugin, Unit>) {
        val optMap = objectPlugins[id] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Object is already bound to a plugin: $id [opt=$opt]")
            throw IllegalStateException()
        }
        optMap[opt] = plugin
        objectPlugins[id] = optMap
        count++
    }

    fun executeObject(p: Player, id: Int, opt: Int): Boolean {
        val optMap = objectPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
    }

    @Throws(IllegalStateException::class)
    fun bindCustomPathingObject(id: Int, plugin: Function1<Plugin, Unit>) {
        if (customPathingObjects.containsKey(id)) {
            logger.error("Object is already bound to a custom path-finder plugin: $id")
            throw IllegalStateException()
        }
        customPathingObjects[id] = plugin
        count++
    }

    fun executeCustomPathingObject(p: Player, id: Int): Boolean {
        val logic = customPathingObjects[id] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
    }
}