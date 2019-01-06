package gg.rsmod.game.plugin

import gg.rsmod.game.model.COMMAND_ARGS_ATTR
import gg.rsmod.game.model.COMMAND_ATTR
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.GameService
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

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
    private var pluginCount = 0

    /**
     * A list of plugins that will be executed upon login.
     */
    private val loginPlugins = arrayListOf<Function1<Plugin, Unit>>()

    /**
     * A map that contains command plugins. The pair has the privilege power
     * required to use the command on the left, and the plugin on the right.
     *
     * The privilege power left value can be set to null, which means anyone
     * can use the command.
     */
    private val commandPlugins = hashMapOf<String, Pair<String?, Function1<Plugin, Unit>>>()

    /**
     * A map that contains plugins that should be executed when the [TimerKey]
     * hits a value of [0] time left.
     */
    private val timerPlugins = hashMapOf<TimerKey, Function1<Plugin, Unit>>()

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
     * A map that contains any plugin that will be executed upon entering a new
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val enterChunkPlugins = hashMapOf<Int, MutableList<Function1<Plugin, Unit>>>()

    /**
     * A map that contains any plugin that will be executed when leaving a
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val exitChunkPlugins = hashMapOf<Int, MutableList<Function1<Plugin, Unit>>>()

    /**
     * A map that contains items and any associated menu-click and its respective
     * plugin executor, if any (would not be in the map if it doesn't have a plugin).
     */
    private val itemPlugins = hashMapOf<Int, HashMap<Int, Function1<Plugin, Unit>>>()

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
    fun init(gameService: GameService, sourcePath: String, packedPath: String) {
        scanForPlugins(sourcePath, packedPath)
        gameService.world.pluginExecutor.init(gameService)
    }

    /**
     * Goes through all the methods found in our [sourcePath] system file path
     * and looks for any [ScanPlugins] annotation to invoke.
     *
     * @throws Exception If the plugin registration function could not be invoked.
     *                   Possible reasons: must be static [JvmStatic]
     */
    @Throws(Exception::class)
    fun scanForPlugins(sourcePath: String, packedPath: String) {
        loginPlugins.clear()
        commandPlugins.clear()
        timerPlugins.clear()
        buttonPlugins.clear()
        enterRegionPlugins.clear()
        exitRegionPlugins.clear()
        enterChunkPlugins.clear()
        exitChunkPlugins.clear()
        itemPlugins.clear()
        objectPlugins.clear()

        pluginCount = 0

        Reflections(sourcePath, SubTypesScanner(false), MethodAnnotationsScanner()).getMethodsAnnotatedWith(ScanPlugins::class.java).forEach { method ->
            if (!method.declaringClass.name.contains("$") && !method.declaringClass.name.endsWith("Package")) {
                try {
                    method.invoke(null, this)
                } catch (e: Exception) {
                    logger.error("Error loading source plugin: ${method.declaringClass} [$method].", e)
                    throw e
                }
            }
        }

        val packed = Paths.get(packedPath)
        val packedUrl = packed.toFile().toURI().toURL()
        Files.walk(packed).forEach { path ->
            if (!path.fileName.toString().endsWith(".jar")) {
                return@forEach
            }
            val urls = arrayOf(packedUrl, path.toFile().toURI().toURL())
            val classLoader = URLClassLoader(urls, PluginRepository::class.java.classLoader)

            val jar = JarFile(path.toFile())
            val entries = jar.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (!entry.name.endsWith(".class") || entry.name.contains("$") || entry.name.endsWith("Package")) {
                    continue
                }
                val clazz = classLoader.loadClass(entry.name.replace("/", ".").replace(".class", ""))
                clazz.methods.forEach { method ->
                    if (method.isAnnotationPresent(ScanPlugins::class.java)) {
                        try {
                            method.invoke(null, this)
                        } catch (e: Exception) {
                            logger.error("Error loading source plugin: ${method.declaringClass} [$method].", e)
                            throw e
                        }
                    }
                }
            }
            jar.close()
        }
    }

    /**
     * Get the total amount of plugins loaded from the plugins path.
     */
    fun getPluginCount(): Int = pluginCount

    fun bindLogin(plugin: Function1<Plugin, Unit>) {
        loginPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogin(p: Player) {
        loginPlugins.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    @Throws(IllegalStateException::class)
    fun bindCommand(command: String, powerRequired: String? = null, plugin: Function1<Plugin, Unit>) {
        val cmd = command.toLowerCase()
        if (commandPlugins.containsKey(cmd)) {
            logger.error("Command is already bound to a plugin: $cmd")
            throw IllegalStateException()
        }
        commandPlugins[cmd] = Pair(powerRequired, plugin)
        pluginCount++
    }

    fun executeCommand(p: Player, command: String, args: Array<String>? = null): Boolean {
        val commandPair = commandPlugins[command]
        if (commandPair != null) {
            val powerRequired = commandPair.first
            val plugin = commandPair.second

            if (powerRequired != null && !p.privilege.powers.contains(powerRequired.toLowerCase())) {
                return false
            }

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
    fun bindTimer(key: TimerKey, plugin: Function1<Plugin, Unit>) {
        if (timerPlugins.containsKey(key)) {
            logger.error("Timer key is already bound to a plugin: $key")
            throw IllegalStateException()
        }
        timerPlugins[key] = plugin
        pluginCount++
    }

    fun executeTimer(pawn: Pawn, key: TimerKey): Boolean {
        val plugin = timerPlugins[key]
        if (plugin != null) {
            pawn.world.pluginExecutor.execute(pawn, plugin)
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
        pluginCount++
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
        pluginCount++
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
        pluginCount++
    }

    fun executeRegionExit(p: Player, regionId: Int) {
        exitRegionPlugins[regionId]?.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    fun bindChunkEnter(chunkHash: Int, plugin: Function1<Plugin, Unit>) {
        val plugins = enterChunkPlugins[chunkHash]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            enterChunkPlugins[chunkHash] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeChunkEnter(p: Player, chunkHash: Int) {
        enterChunkPlugins[chunkHash]?.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    fun bindChunkExit(chunkHash: Int, plugin: Function1<Plugin, Unit>) {
        val plugins = exitChunkPlugins[chunkHash]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            exitChunkPlugins[chunkHash] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeChunkExit(p: Player, chunkHash: Int) {
        exitChunkPlugins[chunkHash]?.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    @Throws(IllegalStateException::class)
    fun bindItem(id: Int, opt: Int, plugin: Function1<Plugin, Unit>) {
        val optMap = itemPlugins[id] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Item is already bound to a plugin: $id [opt=$opt]")
            throw IllegalStateException()
        }
        optMap[opt] = plugin
        itemPlugins[id] = optMap
        pluginCount++
    }

    fun executeItem(p: Player, id: Int, opt: Int): Boolean {
        val optMap = itemPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
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
        pluginCount++
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
        pluginCount++
    }

    fun executeCustomPathingObject(p: Player, id: Int): Boolean {
        val logic = customPathingObjects[id] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
    }
}