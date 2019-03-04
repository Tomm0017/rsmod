package gg.rsmod.game.plugin

import com.google.common.base.Stopwatch
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.*
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.entity.*
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.shop.Shop
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.game.service.GameService
import gg.rsmod.game.service.game.NpcStatsService
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import mu.KotlinLogging
import java.lang.ref.WeakReference
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A repository that is responsible for storing and executing plugins, as well
 * as making sure no plugin collides.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginRepository(val world: World) {

    companion object {
        private val logger = KotlinLogging.logger {  }
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
     * Plugins that get executed when the world is initialised.
     */
    private val worldInitPlugins = arrayListOf<(Plugin).() -> Unit>()

    /**
     * The plugin that will executed when changing display modes.
     */
    private var windowStatusPlugin: ((Plugin).() -> Unit)? = null

    /**
     * The plugin that will be executed when the core module wants
     * close the main modal the player has opened.
     *
     * This is used for things such as the [gg.rsmod.game.message.impl.MoveGameClickMessage].
     */
    private var closeModalPlugin: ((Plugin).() -> Unit)? = null

    /**
     * A list of plugins that will be executed upon login.
     */
    private val loginPlugins = arrayListOf<(Plugin).() -> Unit>()

    /**
     * A list of plugins that will be executed upon logout.
     */
    private val logoutPlugins = arrayListOf<(Plugin).() -> Unit>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * being spawned into the world. Use sparingly.
     */
    private val globalNpcSpawnPlugins = arrayListOf<(Plugin).() -> Unit>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * with a specific id being spawned into the world. Use sparingly per npc.
     *
     * Note: any npc added to this map <strong>will</strong> still invoke the
     * [globalNpcSpawnPlugins] plugin.
     */
    private val npcSpawnPlugins = hashMapOf<Int, MutableList<(Plugin).() -> Unit>>()

    /**
     * The plugin that will handle initiating combat.
     */
    private var combatPlugin: ((Plugin).() -> Unit)? = null

    /**
     * A map of plugins that contain custom combat plugins for specific npcs.
     */
    private val npcCombatPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map of plugins that will handle spells on npcs depending on the interface
     * hash of the spell.
     */
    private val spellOnNpcPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map that contains plugins that should be executed when the [TimerKey]
     * hits a value of [0] time left.
     */
    private val timerPlugins = hashMapOf<TimerKey, (Plugin).() -> Unit>()

    /**
     * A map that contains plugins that should be executed when an interface
     * is opened.
     */
    private val interfaceOpenPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map that contains plugins that should be executed when an interface
     * is closed.
     */
    private val interfaceClosePlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map that contains command plugins. The pair has the privilege power
     * required to use the command on the left, and the plugin on the right.
     *
     * The privilege power left value can be set to null, which means anyone
     * can use the command.
     */
    private val commandPlugins = hashMapOf<String, Pair<String?, (Plugin).() -> Unit>>()

    /**
     * A map of button click plugins. The key is a shifted value of the parent
     * and child id.
     */
    private val buttonPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map of plugins that contain plugins that should execute when equipping
     * items from a certain equipment slot.
     */
    private val equipSlotPlugins: Multimap<Int, (Plugin).() -> Unit> = HashMultimap.create()

    /**
     * A map of plugins that can stop an item from being equipped.
     */
    private val equipItemRequirementPlugins = hashMapOf<Int, (Plugin).() -> Boolean>()

    /**
     * A map of plugins that are executed when a player equips an item.
     */
    private val equipItemPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A map of plugins that are executed when a player un-equips an item.
     */
    private val unequipItemPlugins = hashMapOf<Int, (Plugin).() -> Unit>()

    /**
     * A plugin that executes when a player levels up a skill.
     */
    private var skillLevelUps: ((Plugin).() -> Unit)? = null

    /**
     * A map that contains any plugin that will be executed upon entering a new
     * region. The key is the region id and the value is a list of plugins
     * that will execute upon entering the region.
     */
    private val enterRegionPlugins = hashMapOf<Int, MutableList<(Plugin).() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed upon leaving a region.
     * The key is the region id and the value is a list of plugins that will execute
     * upon leaving the region.
     */
    private val exitRegionPlugins = hashMapOf<Int, MutableList<(Plugin).() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed upon entering a new
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val enterChunkPlugins = hashMapOf<Int, MutableList<(Plugin).() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed when leaving a
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val exitChunkPlugins = hashMapOf<Int, MutableList<(Plugin).() -> Unit>>()

    /**
     * A map that contains items and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val itemPlugins = hashMapOf<Int, HashMap<Int, (Plugin).() -> Unit>>()

    /**
     * A map that contains ground items and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val groundItemPlugins = hashMapOf<Int, HashMap<Int, (Plugin).() -> Unit>>()

    /**
     * A map that contains objects and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val objectPlugins = hashMapOf<Int, HashMap<Int, (Plugin).() -> Unit>>()

    /**
     * A map that contains npcs and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val npcPlugins = hashMapOf<Int, HashMap<Int, (Plugin).() -> Unit>>()

    /**
     * A map that contains npc ids as the key and their interaction distance as
     * the value. If map does not contain an npc, it will have the default interaction
     */
    private val npcInteractionDistancePlugins = hashMapOf<Int, Int>()

    /**
     * A map that contains object ids as the key and their interaction distance as
     * the value. If map does not contain an object, it will have the default interaction
     */
    private val objInteractionDistancePlugins = hashMapOf<Int, Int>()

    /**
     * Temporarily holds the multi-combat area chunks for this [PluginRepository];
     * this is then passed onto the [World] and is cleared.
     *
     * The int value is calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    internal val multiCombatChunks = IntOpenHashSet()

    /**
     * Temporarily holds the multi-combat area regions for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     *
     * The int value is calculated via [gg.rsmod.game.model.Tile.toRegionId].
     */
    internal val multiCombatRegions = IntOpenHashSet()

    /**
     * Temporarily holds all npc spawns set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val npcSpawns = ObjectArrayList<Npc>()

    /**
     * Temporarily holds all object spawns set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val objSpawns = ObjectArrayList<DynamicObject>()

    /**
     * Temporarily holds all ground item spawns set from plugins for this
     * [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val itemSpawns = ObjectArrayList<GroundItem>()

    /**
     * Temporarily holds all npc combat definitions set from plugins for this
     * [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val npcCombatDefs = Int2ObjectOpenHashMap<NpcCombatDef>()

    /**
     * Temporarily holds all valid shops set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val shops = Object2ObjectOpenHashMap<String, Shop>()

    /**
     * Temporarily holds all container keys set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val containerKeys = ObjectOpenHashSet<ContainerKey>()

    /**
     * Initiates and populates all our plugins.
     */
    fun init(jarPluginsDirectory: String, analyzeMode: Boolean) {
        loadPlugins(jarPluginsDirectory, analyzeMode)

        setCombatDefs()
        spawnEntities()
        setMultiAreas()
        setContainers()
        setShops()
    }

    internal fun loadPlugins(jarPluginsDirectory: String, analyzeMode: Boolean) {
        analyzer = if (analyzeMode) PluginAnalyzer(this) else null
        scanPackageForPlugins(world)
        scanJarDirectoryForPlugins(world, Paths.get(jarPluginsDirectory))
        analyzer?.analyze(world)
        analyzer = null
    }

    fun scanPackageForPlugins(world: World) {
        ClassGraph().enableAllInfo().whitelistModules().scan().use { result ->
            val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
            plugins.forEach { p ->
                val pluginClass = p.loadClass(KotlinPlugin::class.java)
                val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java)
                analyzer?.setClass(pluginClass)
                constructor.newInstance(this, world)
            }
        }
    }

    fun scanJarDirectoryForPlugins(world: World, directory: Path) {
        if (Files.exists(directory)) {
            Files.walk(directory).forEach { path ->
                if (!path.fileName.toString().endsWith(".jar")) {
                    return@forEach
                }
                scanJarForPlugins(world, path)
            }
        }
    }

    fun scanJarForPlugins(world: World, path: Path) {
        val urls = arrayOf(path.toFile().toURI().toURL())
        val classLoader = URLClassLoader(urls, PluginRepository::class.java.classLoader)

        ClassGraph().ignoreParentClassLoaders().addClassLoader(classLoader).enableAllInfo().scan().use { result ->
            val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
            plugins.forEach { p ->
                val pluginClass = p.loadClass(KotlinPlugin::class.java)
                val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java)
                analyzer?.setClass(pluginClass)
                constructor.newInstance(this, world)
            }
        }
    }

    private fun setCombatDefs() {
        val service = world.getService(NpcStatsService::class.java) ?: return
        npcCombatDefs.forEach { npc, def ->
            if (service.get(npc) != null) {
                logger.warn { "Npc $npc (${world.definitions.get(NpcDef::class.java, npc).name}) has a set combat definition but has been overwritten by a plugin." }
            }
            service.set(npc, def)
        }
    }

    private fun spawnEntities() {
        npcSpawns.forEach { npc -> world.spawn(npc) }
        npcSpawns.clear()

        objSpawns.forEach { obj -> world.spawn(obj) }
        objSpawns.clear()

        itemSpawns.forEach { item -> world.spawn(item) }
        itemSpawns.clear()
    }

    private fun setShops() {
        shops.forEach { name, shop -> world.shops[name] = shop }
        shops.clear()
    }

    private fun setMultiAreas() {
        world.multiCombatChunks.clear()
        world.multiCombatRegions.clear()
        world.multiCombatChunks.addAll(multiCombatChunks)
        world.multiCombatRegions.addAll(multiCombatRegions)
        multiCombatChunks.clear()
        multiCombatRegions.clear()
    }

    private fun setContainers() {
        world.registeredContainers.addAll(containerKeys)
        containerKeys.clear()
    }

    /**
     * Get the total amount of plugins loaded from the plugins path.
     */
    fun getPluginCount(): Int = pluginCount

    fun getNpcInteractionDistance(npc: Int): Int? = npcInteractionDistancePlugins[npc]

    fun getObjInteractionDistance(obj: Int): Int? = objInteractionDistancePlugins[obj]

    fun bindWorldInit(plugin: (Plugin).() -> Unit) {
        worldInitPlugins.add(plugin)
    }

    fun executeWorldInit(world: World) {
        worldInitPlugins.forEach { logic -> world.executePlugin(world, logic) }
    }

    fun bindCombat(plugin: (Plugin).() -> Unit) {
        if (combatPlugin != null) {
            logger.error("Combat plugin is already bound")
            throw IllegalStateException("Combat plugin is already bound")
        }
        combatPlugin = plugin
    }

    fun executeCombat(pawn: Pawn) {
        if (combatPlugin != null) {
            pawn.executePlugin(combatPlugin!!)
        }
    }

    fun bindNpcCombat(npc: Int, plugin: (Plugin).() -> Unit) {
        if (npcCombatPlugins.containsKey(npc)) {
            logger.error("Npc is already bound to a combat plugin: $npc")
            throw IllegalStateException("Npc is already bound to a combat plugin: $npc")
        }
        npcCombatPlugins[npc] = plugin
        pluginCount++
    }

    fun executeNpcCombat(n: Npc): Boolean {
        val plugin = npcCombatPlugins[n.id] ?: return false
        n.executePlugin(plugin)
        return true
    }

    fun bindSpellOnNpc(parent: Int, child: Int, plugin: (Plugin).() -> Unit) {
        val hash = (parent shl 16) or child
        if (spellOnNpcPlugins.containsKey(hash)) {
            logger.error("Spell is already bound to a plugin: [$parent, $child]")
            throw IllegalStateException("Spell is already bound to a plugin: [$parent, $child]")
        }
        spellOnNpcPlugins[hash] = plugin
        pluginCount++
    }

    fun executeSpellOnNpc(p: Player, parent: Int, child: Int): Boolean {
        val hash = (parent shl 16) or child
        val plugin = spellOnNpcPlugins[hash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindWindowStatus(plugin: (Plugin).() -> Unit) {
        if (windowStatusPlugin != null) {
            logger.error("Window status is already bound to a plugin")
            throw IllegalStateException("Window status is already bound to a plugin")
        }
        windowStatusPlugin = plugin
    }

    fun executeWindowStatus(p: Player) {
        if (windowStatusPlugin != null) {
            p.executePlugin(windowStatusPlugin!!)
        } else {
            logger.warn { "Window status is not bound to a plugin." }
        }
    }

    fun bindModalClose(plugin: (Plugin).() -> Unit) {
        if (closeModalPlugin != null) {
            logger.error("Modal close is already bound to a plugin")
            throw IllegalStateException("Modal close is already bound to a plugin")
        }
        closeModalPlugin = plugin
    }

    fun executeModalClose(p: Player) {
        if (closeModalPlugin != null) {
            p.executePlugin(closeModalPlugin!!)
        } else {
            logger.warn { "Modal close is not bound to a plugin." }
        }
    }

    fun bindLogin(plugin: (Plugin).() -> Unit) {
        loginPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogin(p: Player) {
        loginPlugins.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindLogout(plugin: (Plugin).() -> Unit) {
        logoutPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogout(p: Player) {
        logoutPlugins.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindGlobalNpcSpawn(plugin: (Plugin).() -> Unit) {
        globalNpcSpawnPlugins.add(plugin)
        pluginCount++
    }

    fun bindNpcSpawn(npc: Int, plugin: (Plugin).() -> Unit) {
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
            customPlugins.forEach { logic -> n.executePlugin(logic) }
        }
        globalNpcSpawnPlugins.forEach { logic -> n.executePlugin(logic) }
    }

    fun bindTimer(key: TimerKey, plugin: (Plugin).() -> Unit) {
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
            pawn.executePlugin(plugin)
            return true
        }
        return false
    }

    fun executeWorldTimer(world: World, key: TimerKey): Boolean {
        val plugin = timerPlugins[key]
        if (plugin != null) {
            world.executePlugin(world, plugin)
            return true
        }
        return false
    }

    fun bindInterfaceOpen(interfaceId: Int, plugin: (Plugin).() -> Unit) {
        if (interfaceOpenPlugins.containsKey(interfaceId)) {
            logger.error("Component id is already bound to a plugin: $interfaceId")
            throw IllegalStateException("Component id is already bound to a plugin: $interfaceId")
        }
        interfaceOpenPlugins[interfaceId] = plugin
        pluginCount++
    }

    fun executeInterfaceOpen(p: Player, interfaceId: Int): Boolean {
        val plugin = interfaceOpenPlugins[interfaceId]
        if (plugin != null) {
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindInterfaceClose(interfaceId: Int, plugin: (Plugin).() -> Unit) {
        if (interfaceClosePlugins.containsKey(interfaceId)) {
            logger.error("Component id is already bound to a plugin: $interfaceId")
            throw IllegalStateException("Component id is already bound to a plugin: $interfaceId")
        }
        interfaceClosePlugins[interfaceId] = plugin
        pluginCount++
    }

    fun executeInterfaceClose(p: Player, interfaceId: Int): Boolean {
        val plugin = interfaceClosePlugins[interfaceId]
        if (plugin != null) {
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindCommand(command: String, powerRequired: String? = null, plugin: (Plugin).() -> Unit) {
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
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindButton(parent: Int, child: Int, plugin: (Plugin).() -> Unit) {
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
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindEquipSlot(equipSlot: Int, plugin: (Plugin).() -> Unit) {
        equipSlotPlugins.put(equipSlot, plugin)
        pluginCount++
    }

    fun executeEquipSlot(p: Player, equipSlot: Int): Boolean {
        val plugin = equipSlotPlugins[equipSlot]
        if (plugin != null) {
            plugin.forEach { logic -> p.executePlugin(logic) }
            return true
        }
        return false
    }

    fun bindEquipItemRequirement(item: Int, plugin: (Plugin).() -> Boolean) {
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
            return p.executePlugin(plugin) == true
        }
        /**
         * Should always be able to wear items by default.
         */
        return true
    }

    fun bindEquipItem(item: Int, plugin: (Plugin).() -> Unit) {
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
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindUnequipItem(item: Int, plugin: (Plugin).() -> Unit) {
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
            p.executePlugin(plugin)
            return true
        }
        return false
    }

    fun bindSkillLevelUp(plugin: Plugin.() -> Unit) {
        check(skillLevelUps == null) { "Skill level up logic already set." }
        skillLevelUps = plugin
    }

    fun executeSkillLevelUp(p: Player) {
        skillLevelUps?.let { p.executePlugin(it) }
    }

    fun bindRegionEnter(regionId: Int, plugin: (Plugin).() -> Unit) {
        val plugins = enterRegionPlugins[regionId]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            enterRegionPlugins[regionId] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeRegionEnter(p: Player, regionId: Int) {
        enterRegionPlugins[regionId]?.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindRegionExit(regionId: Int, plugin: (Plugin).() -> Unit) {
        val plugins = exitRegionPlugins[regionId]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            exitRegionPlugins[regionId] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeRegionExit(p: Player, regionId: Int) {
        exitRegionPlugins[regionId]?.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindChunkEnter(chunkHash: Int, plugin: (Plugin).() -> Unit) {
        val plugins = enterChunkPlugins[chunkHash]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            enterChunkPlugins[chunkHash] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeChunkEnter(p: Player, chunkHash: Int) {
        enterChunkPlugins[chunkHash]?.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindChunkExit(chunkHash: Int, plugin: (Plugin).() -> Unit) {
        val plugins = exitChunkPlugins[chunkHash]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            exitChunkPlugins[chunkHash] = arrayListOf(plugin)
        }
        pluginCount++
    }

    fun executeChunkExit(p: Player, chunkHash: Int) {
        exitChunkPlugins[chunkHash]?.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindItem(id: Int, opt: Int, plugin: (Plugin).() -> Unit) {
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
        p.executePlugin(logic)
        return true
    }

    fun bindGroundItem(id: Int, opt: Int, plugin: (Plugin).() -> Unit) {
        val optMap = groundItemPlugins[id] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Ground item is already bound to a plugin: $id [opt=$opt]")
            throw IllegalStateException("Ground item is already bound to a plugin: $id [opt=$opt]")
        }
        optMap[opt] = plugin
        groundItemPlugins[id] = optMap
        pluginCount++
    }

    fun executeGroundItem(p: Player, id: Int, opt: Int): Boolean {
        val optMap = groundItemPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.executePlugin(logic)
        return true
    }

    fun bindObject(obj: Int, opt: Int, lineOfSightDistance: Int = -1, plugin: (Plugin).() -> Unit) {
        val optMap = objectPlugins[obj] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Object is already bound to a plugin: $obj [opt=$opt]")
            throw IllegalStateException("Object is already bound to a plugin: $obj [opt=$opt]")
        }

        if (lineOfSightDistance != -1) {
            objInteractionDistancePlugins[obj] = lineOfSightDistance
        }

        optMap[opt] = plugin
        objectPlugins[obj] = optMap
        pluginCount++
    }

    fun executeObject(p: Player, id: Int, opt: Int): Boolean {
        val optMap = objectPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.executePlugin(logic)
        return true
    }

    fun bindNpc(npc: Int, opt: Int, lineOfSightDistance: Int = -1, plugin: (Plugin).() -> Unit) {
        val optMap = npcPlugins[npc] ?: HashMap()
        if (optMap.containsKey(opt)) {
            logger.error("Npc is already bound to a plugin: $npc [opt=$opt]")
            throw IllegalStateException("Npc is already bound to a plugin: $npc [opt=$opt]")
        }

        if (lineOfSightDistance != -1) {
            npcInteractionDistancePlugins[npc] = lineOfSightDistance
        }

        optMap[opt] = plugin
        npcPlugins[npc] = optMap
        pluginCount++
    }

    fun executeNpc(p: Player, id: Int, opt: Int): Boolean {
        val optMap = npcPlugins[id] ?: return false
        val logic = optMap[opt] ?: return false
        p.executePlugin(logic)
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
            world.getService(GameService::class.java, false)?.let { s -> s.pause = true }
            val warmupStart = Stopwatch.createStarted()
            for (i in 0 until 10_000) {
                // Note(Tom): we could also use future to do this just in case
                // one of the plugins has a deadlock.
                executePlugins(world, warmup = true)
                if (warmupStart.elapsed(TimeUnit.SECONDS) >= 10) {
                    break
                }
            }
            world.getService(GameService::class.java, false)?.let { s -> s.pause = false }

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


            stopwatch.reset().start()
            repository.executeLogout(dummy)
            times.add(TimedPlugin(name = "Logout", note = "", time = stopwatch.elapsed(measurement)))

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
            repository.interfaceClosePlugins.forEach { hash, _ ->
                repository.executeInterfaceClose(dummy, hash shr 16)
            }
            times.add(TimedPlugin(name = "Close Component", note = "", time = stopwatch.elapsed(measurement)))
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
                    dummy.attr[INTERACTING_ITEM] = WeakReference(Item(item))
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
                    dummy.attr[INTERACTING_OBJ_ATTR] = WeakReference(DynamicObject(obj, 10, 0, Tile(0, 0)))
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