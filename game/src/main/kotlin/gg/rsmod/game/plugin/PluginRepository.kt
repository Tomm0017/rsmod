package gg.rsmod.game.plugin

import com.google.common.base.Stopwatch
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.model.*
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.service.GameService
import io.github.classgraph.ClassGraph
import org.apache.logging.log4j.LogManager
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.jar.JarFile

/**
 * A repository that is responsible for storing and executing plugins, as well
 * as making sure no plugin collides.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginRepository(val world: World) {

    companion object {
        private val logger = LogManager.getLogger(PluginRepository::class.java)
    }

    /**
     * The [PluginAnalyzer] used to list some specs regarding the current
     * loaded plugins.
     */
    private var analyzer: PluginAnalyzer? = null

    /**
     * The total amount of plugins.
     */
    private var pluginCount = 0

    /**
     * The plugin that will execute when changing display modes.
     */
    private var displayModePlugin: Function1<Plugin, Unit>? = null

    /**
     * A list of plugins that will be executed upon login.
     */
    private val loginPlugins = arrayListOf<Function1<Plugin, Unit>>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * being spawned into the world. Use sparingly.
     */
    private val globalNpcSpawnPlugins = arrayListOf<Function1<Plugin, Unit>>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * with a specific id being spawned into the world. Use sparingly.
     *
     * Note: any npc added to this map will <strong>not</strong> call the
     * [globalNpcSpawnPlugins] plugin.
     */
    private val npcSpawnPlugins = hashMapOf<Int, MutableList<Function1<Plugin, Unit>>>()

    /**
     * The plugin that will handle initiating combat.
     */
    private var combatPlugin: Function1<Plugin, Unit>? = null

    /**
     * A map of plugins that contain custom combat plugins for specific npcs.
     */
    private val npcCombatPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map of plugins that will handle spells on npcs depending on the interface
     * hash of the spell.
     */
    private val spellOnNpcPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map that contains plugins that should be executed when the [TimerKey]
     * hits a value of [0] time left.
     */
    private val timerPlugins = hashMapOf<TimerKey, Function1<Plugin, Unit>>()

    /**
     * A map that contains plugins that should be executed when an interface
     * is closed.
     */
    private val interfaceClose = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map that contains command plugins. The pair has the privilege power
     * required to use the command on the left, and the plugin on the right.
     *
     * The privilege power left value can be set to null, which means anyone
     * can use the command.
     */
    private val commandPlugins = hashMapOf<String, Pair<String?, Function1<Plugin, Unit>>>()

    /**
     * A map of button click plugins. The key is a shifted value of the parent
     * and child id.
     */
    private val buttonPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map of plugins that contain plugins that should execute when equipping
     * items from a certain equipment slot.
     */
    private val equipSlotPlugins: Multimap<Int, Function1<Plugin, Unit>> = HashMultimap.create()

    /**
     * A map of plugins that can stop an item from being equipped.
     */
    private val equipItemRequirementPlugins = hashMapOf<Int, Function1<Plugin, Boolean>>()

    /**
     * A map of plugins that are executed when a player equips an item.
     */
    private val equipItemPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map of plugins that are executed when a player un-equips an item.
     */
    private val unequipItemPlugins = hashMapOf<Int, Function1<Plugin, Unit>>()

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
     * A map that contains npcs and any associated menu-click and its respective
     * plugin executor, if any (would not be in the map if it doesn't have a plugin).
     */
    private val npcPlugins = hashMapOf<Int, HashMap<Int, Function1<Plugin, Unit>>>()

    /**
     * A map of objects that have custom path finding. This means that the plugin
     * is responsible for walking to the object if necessary.
     */
    private val customObjectPaths = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * A map of npcs that have custom path finding. This means that the plugin
     * is responsible for walking to the npc if necessary.
     */
    private val customNpcPaths = hashMapOf<Int, Function1<Plugin, Unit>>()

    /**
     * Initiates and populates all our plugins.
     */
    fun init(gameService: GameService, packedPath: String, analyzeMode: Boolean) {
        gameService.world.pluginExecutor.init(gameService)
        scanForPlugins(packedPath, gameService.world, analyzeMode)
    }

    @Throws(Exception::class)
    fun scanForPlugins(packedPath: String, world: World, analyzeMode: Boolean) {
        analyzer = if (analyzeMode) PluginAnalyzer(this) else null

        displayModePlugin = null
        combatPlugin = null
        loginPlugins.clear()
        globalNpcSpawnPlugins.clear()
        npcSpawnPlugins.clear()
        npcCombatPlugins.clear()
        spellOnNpcPlugins.clear()
        timerPlugins.clear()
        interfaceClose.clear()
        commandPlugins.clear()
        buttonPlugins.clear()
        equipSlotPlugins.clear()
        equipItemRequirementPlugins.clear()
        equipItemPlugins.clear()
        unequipItemPlugins.clear()
        enterRegionPlugins.clear()
        exitRegionPlugins.clear()
        enterChunkPlugins.clear()
        exitChunkPlugins.clear()
        itemPlugins.clear()
        objectPlugins.clear()
        npcPlugins.clear()
        customObjectPaths.clear()
        customNpcPaths.clear()

        pluginCount = 0

        ClassGraph().enableAllInfo().whitelistModules().scan().use { result ->
            val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
            plugins.forEach { p ->
                val pluginClass = p.loadClass(KotlinPlugin::class.java)
                val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java)
                analyzer?.setClass(pluginClass)
                constructor.newInstance(this, world)
            }
        }

        val packed = Paths.get(packedPath)
        if (Files.exists(packed)) {
            Files.walk(packed).forEach { path ->
                if (!path.fileName.toString().endsWith(".jar")) {
                    return@forEach
                }
                scanJarForPlugins(world, path)
            }
        }

        analyzer?.analyze(world)
        analyzer = null
    }

    fun scanJarForPlugins(world: World, path: Path) {
        val urls = arrayOf(path.toFile().toURI().toURL())
        val classLoader = URLClassLoader(urls, PluginRepository::class.java.classLoader)

        val jar = JarFile(path.toFile())
        val entries = jar.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (!entry.name.endsWith(".class") || entry.name.contains("$") || entry.name.endsWith("Package")) {
                continue
            }
            val clazz = classLoader.loadClass(entry.name.replace("/", ".").replace(".class", ""))

            ClassGraph().ignoreParentClassLoaders().addClassLoader(classLoader).enableAllInfo().scan().use { result ->
                val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
                plugins.forEach { p ->
                    val pluginClass = p.loadClass(KotlinPlugin::class.java)
                    val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java)
                    analyzer?.setClass(clazz)
                    constructor.newInstance(this, world)
                }
            }
        }
        jar.close()
    }

    /**
     * Get the total amount of plugins loaded from the plugins path.
     */
    fun getPluginCount(): Int = pluginCount

    fun bindCombat(plugin: Function1<Plugin, Unit>) {
        if (combatPlugin != null) {
            logger.error("Combat plugin is already bound")
            throw IllegalStateException("Combat plugin is already bound")
        }
        combatPlugin = plugin
    }

    fun executeCombat(pawn: Pawn) {
        if (combatPlugin != null) {
            pawn.world.pluginExecutor.execute(pawn, combatPlugin!!)
        }
    }

    fun bindNpcCombat(npc: Int, plugin: Function1<Plugin, Unit>) {
        if (npcCombatPlugins.containsKey(npc)) {
            logger.error("Npc is already bound to a combat plugin: $npc")
            throw IllegalStateException("Npc is already bound to a combat plugin: $npc")
        }
        npcCombatPlugins[npc] = plugin
        pluginCount++
    }

    fun executeNpcCombat(n: Npc): Boolean {
        val plugin = npcCombatPlugins[n.id] ?: return false
        n.world.pluginExecutor.execute(n, plugin)
        return true
    }

    fun bindSpellOnNpc(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) {
        val hash = (parent shl 16) or child
        if (spellOnNpcPlugins.containsKey(hash)) {
            logger.error("Spell is already bound to a plugin: [$parent, $child]")
            throw IllegalStateException("Spell is already bound to a plugin: [$parent, $child]")
        }
        spellOnNpcPlugins[hash] = plugin
        pluginCount++
    }

    fun executeSpellOnNpc(p: Player, interfaceId: Int, child: Int): Boolean {
        val hash = (interfaceId shl 16) or child
        val plugin = spellOnNpcPlugins[hash] ?: return false
        p.world.pluginExecutor.execute(p, plugin)
        return true
    }

    fun bindDisplayModeChange(plugin: Function1<Plugin, Unit>) {
        if (displayModePlugin != null) {
            logger.error("Display mode change is already bound to a plugin")
            throw IllegalStateException("Display mode change is already bound to a plugin")
        }
        displayModePlugin = plugin
    }

    fun executeDisplayModeChange(p: Player) {
        if (displayModePlugin != null) {
            p.world.pluginExecutor.execute(p, displayModePlugin!!)
        }
    }

    fun bindLogin(plugin: Function1<Plugin, Unit>) {
        loginPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogin(p: Player) {
        loginPlugins.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
    }

    fun bindGlobalNpcSpawn(plugin: Function1<Plugin, Unit>) {
        globalNpcSpawnPlugins.add(plugin)
        pluginCount++
    }

    /**
     * Binding an individual npc's spawn with a plugin will make it so [globalNpcSpawnPlugins]
     * does not get invoked when an npc with [npc] id is spawned. This functionality may or may
     * not be kept.
     */
    fun bindNpcSpawn(npc: Int, plugin: Function1<Plugin, Unit>) {
        val plugins = npcSpawnPlugins[npc]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            npcSpawnPlugins[npc] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeNpcSpawn(n: Npc) {
        val customPlugins = npcSpawnPlugins[n.id]
        if (customPlugins != null && customPlugins.isNotEmpty()) {
            customPlugins.forEach { logic -> n.world.pluginExecutor.execute(n, logic) }
        } else {
            globalNpcSpawnPlugins.forEach { logic -> n.world.pluginExecutor.execute(n, logic) }
        }
    }

    @Throws(IllegalStateException::class)
    fun bindTimer(key: TimerKey, plugin: Function1<Plugin, Unit>) {
        if (timerPlugins.containsKey(key)) {
            logger.error("Timer key is already bound to a plugin: $key")
            throw IllegalStateException("Timer key is already bound to a plugin: $key")
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
    fun bindInterfaceClose(interfaceId: Int, plugin: Function1<Plugin, Unit>) {
        if (interfaceClose.containsKey(interfaceId)) {
            logger.error("Interface id is already bound to a plugin: $interfaceId")
            throw IllegalStateException("Interface id is already bound to a plugin: $interfaceId")
        }
        interfaceClose[interfaceId] = plugin
        pluginCount++
    }

    fun executeInterfaceClose(p: Player, parent: Int): Boolean {
        val plugin = interfaceClose[parent]
        if (plugin != null) {
            p.world.pluginExecutor.execute(p, plugin)
            return true
        }
        return false
    }

    @Throws(IllegalStateException::class)
    fun bindCommand(command: String, powerRequired: String? = null, plugin: Function1<Plugin, Unit>) {
        val cmd = command.toLowerCase()
        if (commandPlugins.containsKey(cmd)) {
            logger.error("Command is already bound to a plugin: $cmd")
            throw IllegalStateException("Command is already bound to a plugin: $cmd")
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
    fun bindButton(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) {
        val hash = (parent shl 16) or child
        if (buttonPlugins.containsKey(hash)) {
            logger.error("Button hash already bound to a plugin: [parent=$parent, child=$child]")
            throw IllegalStateException("Button hash already bound to a plugin: [parent=$parent, child=$child]")
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

    @Throws(IllegalStateException::class)
    fun bindEquipSlot(equipSlot: Int, plugin: Function1<Plugin, Unit>) {
        equipSlotPlugins.put(equipSlot, plugin)
        pluginCount++
    }

    fun executeEquipSlot(p: Player, equipSlot: Int): Boolean {
        val plugin = equipSlotPlugins[equipSlot]
        if (plugin != null) {
            plugin.forEach { logic -> p.world.pluginExecutor.execute(p, logic) }
            return true
        }
        return false
    }

    @Throws(IllegalStateException::class)
    fun bindEquipItemRequirement(item: Int, plugin: Function1<Plugin, Boolean>) {
        if (equipItemRequirementPlugins.containsKey(item)) {
            logger.error("Equip item requirement already bound to a plugin: [item=$item]")
            throw IllegalStateException("Equip item requirement already bound to a plugin: [item=$item]")
        }
        equipItemRequirementPlugins[item] = plugin
        pluginCount++
    }

    fun executeEquipItemRequirement(p: Player, item: Int): Boolean {
        val plugin = equipItemRequirementPlugins[item]
        if (plugin != null) {
            /**
             * Plugin returns true if the item can be equipped, false if it
             * should block the item from being equipped.
             */
            return p.world.pluginExecutor.execute(p, plugin)
        }
        /**
         * Should always be able to wear items by default.
         */
        return true
    }

    @Throws(IllegalStateException::class)
    fun bindEquipItem(item: Int, plugin: Function1<Plugin, Unit>) {
        if (equipItemPlugins.containsKey(item)) {
            logger.error("Equip item already bound to a plugin: [item=$item]")
            throw IllegalStateException("Equip item already bound to a plugin: [item=$item]")
        }
        equipItemPlugins[item] = plugin
        pluginCount++
    }

    fun executeEquipItem(p: Player, item: Int): Boolean {
        val plugin = equipItemPlugins[item]
        if (plugin != null) {
            p.world.pluginExecutor.execute(p, plugin)
            return true
        }
        return false
    }

    @Throws(IllegalStateException::class)
    fun bindUnequipItem(item: Int, plugin: Function1<Plugin, Unit>) {
        if (unequipItemPlugins.containsKey(item)) {
            logger.error("Unequip item already bound to a plugin: [item=$item]")
            throw IllegalStateException("Unequip item already bound to a plugin: [item=$item]")
        }
        unequipItemPlugins[item] = plugin
        pluginCount++
    }

    fun executeUnequipItem(p: Player, item: Int): Boolean {
        val plugin = unequipItemPlugins[item]
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
            throw IllegalStateException("Item is already bound to a plugin: $id [opt=$opt]")
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
            throw IllegalStateException("Object is already bound to a plugin: $id [opt=$opt]")
        }
        optMap[opt] = plugin
        objectPlugins[id] = optMap
        pluginCount++
    }

    fun executeNpc(p: Player, id: Int, opt: Int): Boolean {
        val optMap = npcPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
    }

    @Throws(IllegalStateException::class)
    fun bindNpc(id: Int, opt: Int, plugin: Function1<Plugin, Unit>) {
        val optMap = npcPlugins[id] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Npc is already bound to a plugin: $id [opt=$opt]")
            throw IllegalStateException("Npc is already bound to a plugin: $id [opt=$opt]")
        }
        optMap[opt] = plugin
        npcPlugins[id] = optMap
        pluginCount++
    }

    fun executeObject(p: Player, id: Int, opt: Int): Boolean {
        val optMap = objectPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.world.pluginExecutor.execute(p, logic)
        return true
    }

    @Throws(IllegalStateException::class)
    fun bindCustomObjectPath(id: Int, plugin: Function1<Plugin, Unit>) {
        if (customObjectPaths.containsKey(id)) {
            logger.error("Object is already bound to a custom path-finder plugin: $id")
            throw IllegalStateException("Object is already bound to a custom path-finder plugin: $id")
        }
        customObjectPaths[id] = plugin
        pluginCount++
    }

    fun executeCustomObjectPath(pawn: Pawn, id: Int): Boolean {
        val logic = customObjectPaths[id] ?: return false
        pawn.world.pluginExecutor.execute(pawn, logic)
        return true
    }

    @Throws(IllegalStateException::class)
    fun bindCustomNpcPath(id: Int, plugin: Function1<Plugin, Unit>) {
        if (customNpcPaths.containsKey(id)) {
            logger.error("Npc is already bound to a custom path-finder plugin: $id")
            throw IllegalStateException("Npc is already bound to a custom path-finder plugin: $id")
        }
        customNpcPaths[id] = plugin
        pluginCount++
    }

    fun executeCustomNpcPath(pawn: Pawn, id: Int): Boolean {
        val logic = customNpcPaths[id] ?: return false
        pawn.world.pluginExecutor.execute(pawn, logic)
        return true
    }

    private class PluginAnalyzer(private val repository: PluginRepository) {

        private var classPluginCount = hashMapOf<Class<*>, Int>()

        private var lastKnownPluginCount = 0

        private var currentClass: Class<*>? = null

        fun setClass(clazz: Class<*>?) {
            if (currentClass != null && currentClass != clazz) {
                classPluginCount[currentClass!!] = repository.getPluginCount() - lastKnownPluginCount
                lastKnownPluginCount = repository.getPluginCount()
            }

            this.currentClass = clazz
        }

        fun analyze(world: World) {
            /**
             * Make sure to log the last class if necessary.
             */
            setClass(null)

            println("/*******************************************************/")
            println("                Plugin Analyzing Report                  ")

            println()
            println()

            println("\tWarming up JVM before analyzing...")
            world.getService(GameService::class.java, false).ifPresent { s -> s.pause = true }
            val warmupStart = Stopwatch.createStarted()
            for (i in 0 until 10_000) {
                // Note(Tom): we could also use future to do this just in case
                // one of the plugins has a deadlock.
                executePlugins(world, warmup = true)
                if (warmupStart.elapsed(TimeUnit.SECONDS) >= 10) {
                    break
                }
            }
            world.getService(GameService::class.java, false).ifPresent { s -> s.pause = false }

            println()
            println()

            System.out.format("\t%-25s%-15s%-15s\n", "File", "Plugins", "Class")
            println("\t------------------------------------------------------------------------------------------------")
            classPluginCount.toList().sortedByDescending { it.second }.toMap().forEach { clazz, plugins ->
                System.out.format("\t%-25s%-15d%-30s\n", clazz.simpleName.toLowerCase().replace("_plugin", ""), plugins, clazz.toString().toLowerCase())
            }

            println()
            println()


            System.out.format("\t%-25s%-15s%-15s\n", "Plugin", "Invoke time", "Notes")
            println("\t------------------------------------------------------------------------------------------------")

            executePlugins(world, warmup = false)
            world.pluginExecutor.internalKillAll()

            println()
            println()

            println("/*******************************************************/")
        }

        private fun executePlugins(world: World, warmup: Boolean) {
            val times = arrayListOf<TimedPlugin>()
            val dummy = Player(world)
            val stopwatch = Stopwatch.createUnstarted()
            val individualTimes = arrayListOf<TimedPlugin>()
            val individualStopwatch = Stopwatch.createUnstarted()

            val measurement = TimeUnit.MILLISECONDS
            val measurementName = "ms"
            /**
             * If the time elapsed for a plugin type exceeds this value, relative
             * to the measurement, then we log it further.
             */
            val timeThreshold = 50 // Relative to [measurement]

            stopwatch.reset().start()
            repository.executeLogin(dummy)
            times.add(TimedPlugin(name = "Login", note = "", time = stopwatch.elapsed(measurement)))

            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            /**
             * Most timers take more than one execute to do certain logic,
             * so let's try to find a sweet spot in how many times we should
             * execute timers to get a more accurate execution time.
             */
            val timerExecutions = 1000
            stopwatch.reset().start()
            repository.timerPlugins.forEach { plugin ->
                for (i in 0 until timerExecutions) {
                    repository.executeTimer(dummy, plugin.key)
                }
            }
            times.add(TimedPlugin(name = "Timer", note = "${DecimalFormat().format(timerExecutions)} executions each", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.interfaceClose.forEach { hash, _ ->
                repository.executeInterfaceClose(dummy, hash shr 16)
            }
            times.add(TimedPlugin(name = "Close Interface", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.buttonPlugins.forEach { hash, _ ->
                dummy.attr[INTERACTING_OPT_ATTR] = 0
                dummy.attr[INTERACTING_ITEM_ID] = 0
                dummy.attr[INTERACTING_SLOT_ATTR] = 0
                repository.executeButton(dummy, hash shr 16, hash and 0xFFFF)
            }
            times.add(TimedPlugin(name = "Click Button", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.equipSlotPlugins.forEach { slot, _ ->
                repository.executeEquipSlot(dummy, slot)
            }
            times.add(TimedPlugin(name = "Equip Slot", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.equipItemRequirementPlugins.forEach { item, _ ->
                repository.executeEquipItemRequirement(dummy, item)
            }
            times.add(TimedPlugin(name = "Equip Requirement", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.equipItemPlugins.forEach { item, _ ->
                repository.executeEquipItem(dummy, item)
            }
            times.add(TimedPlugin(name = "Equip Item", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.unequipItemPlugins.forEach { item, _ ->
                repository.executeUnequipItem(dummy, item)
            }
            times.add(TimedPlugin(name = "Unequip Item", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.enterRegionPlugins.forEach { region, _ ->
                repository.executeRegionEnter(dummy, region)
            }
            times.add(TimedPlugin(name = "Enter Region", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.exitRegionPlugins.forEach { region, _ ->
                repository.executeRegionExit(dummy, region)
            }
            times.add(TimedPlugin(name = "Exit Region", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.enterChunkPlugins.forEach { chunk, _ ->
                repository.executeChunkEnter(dummy, chunk)
            }
            times.add(TimedPlugin(name = "Enter Chunk", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.exitChunkPlugins.forEach { chunk, _ ->
                repository.executeChunkExit(dummy, chunk)
            }
            times.add(TimedPlugin(name = "Exit Chunk", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
            }
            times.clear()

            stopwatch.reset().start()
            repository.itemPlugins.forEach { item, map ->
                map.keys.forEach { opt ->
                    individualStopwatch.reset().start()
                    dummy.attr[INTERACTING_ITEM_SLOT] = opt
                    dummy.attr[INTERACTING_ITEM_ID] = item
                    dummy.attr[INTERACTING_ITEM] = Item(item)
                    repository.executeItem(dummy, item, opt)
                    individualTimes.add(TimedPlugin(name = "Item Action", note = "id=$item, opt=$opt", time = individualStopwatch.elapsed(measurement)))
                }
            }
            times.add(TimedPlugin(name = "Item Action", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
                if (times.sumBy { it.time.toInt() } >= timeThreshold) {
                    individualTimes.sortByDescending { it.time }
                    individualTimes.forEach { time -> System.out.format("\t\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
                }
            }
            times.clear()
            individualTimes.clear()

            stopwatch.reset().start()
            repository.objectPlugins.forEach { obj, map ->
                map.keys.forEach { opt ->
                    individualStopwatch.reset().start()
                    dummy.attr[INTERACTING_OBJ_ATTR] = DynamicObject(obj, 10, 0, Tile(0, 0))
                    dummy.attr[INTERACTING_OPT_ATTR] = 0
                    repository.executeObject(dummy, obj, opt)
                    individualTimes.add(TimedPlugin(name = "Object Action", note = "id=$obj, opt=$opt", time = individualStopwatch.elapsed(measurement)))
                }
            }
            times.add(TimedPlugin(name = "Object Action", note = "", time = stopwatch.elapsed(measurement)))
            if (!warmup) {
                times.forEach { time -> System.out.format("\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
                if (times.sumBy { it.time.toInt() } >= timeThreshold) {
                    individualTimes.sortByDescending { it.time }
                    individualTimes.forEach { time -> System.out.format("\t\t%-25s%-15s%-15s\n", time.name, time.time.toString() + " " + measurementName, time.note) }
                }
            }
            times.clear()
            individualTimes.clear()
        }

        private data class TimedPlugin(val name: String, val note: String, val time: Long)
    }
}