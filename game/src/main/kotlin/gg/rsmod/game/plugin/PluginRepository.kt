package gg.rsmod.game.plugin

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.Server
import gg.rsmod.game.event.Event
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.COMMAND_ARGS_ATTR
import gg.rsmod.game.model.attr.COMMAND_ATTR
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.container.key.BANK_KEY
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.container.key.EQUIPMENT_KEY
import gg.rsmod.game.model.container.key.INVENTORY_KEY
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.shop.Shop
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import io.github.classgraph.ClassGraph
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import mu.KLogging
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A repository that is responsible for storing and executing plugins, as well
 * as making sure no plugin collides.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PluginRepository(val world: World) {

    /**
     * The total amount of plugins.
     */
    private var pluginCount = 0

    /**
     * Plugins that get executed when the world is initialised.
     */
    private val worldInitPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * The plugin that will executed when changing display modes.
     */
    private var windowStatusPlugin: (Plugin.() -> Unit)? = null

    /**
     * The plugin that will be executed when the core module wants
     * close the main modal the player has opened.
     *
     * This is used for things such as the [gg.rsmod.game.message.impl.MoveGameClickMessage].
     */
    private var closeModalPlugin: (Plugin.() -> Unit)? = null

    /**
     * This plugin is used to check if a player has a menu opened and any
     * [gg.rsmod.game.model.queue.QueueTask] with a [gg.rsmod.game.model.queue.TaskPriority.STANDARD]
     * priority should wait before executing.
     */
    private var isMenuOpenedPlugin: (Plugin.() -> Boolean)? = null

    /**
     * A list of plugins that will be executed upon login.
     */
    private val loginPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be executed upon logout.
     */
    private val logoutPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * being spawned into the world. Use sparingly.
     */
    private val globalNpcSpawnPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be executed upon an [gg.rsmod.game.model.entity.Npc]
     * with a specific id being spawned into the world. Use sparingly per npc.
     *
     * Note: any npc added to this map <strong>will</strong> still invoke the
     * [globalNpcSpawnPlugins] plugin.
     */
    private val npcSpawnPlugins = Int2ObjectOpenHashMap<MutableList<Plugin.() -> Unit>>()

    /**
     * The plugin that will handle initiating combat.
     */
    private var combatPlugin: (Plugin.() -> Unit)? = null

    /**
     * A map of plugins that contain custom combat plugins for specific npcs.
     */
    private val npcCombatPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map of plugins that will handle spells on npcs depending on the interface
     * hash of the spell.
     */
    private val spellOnNpcPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains plugins that should be executed when the [TimerKey]
     * hits a value of [0] time left.
     */
    private val timerPlugins = hashMapOf<TimerKey, Plugin.() -> Unit>()

    /**
     * A map that contains plugins that should be executed when an interface
     * is opened.
     */
    private val interfaceOpenPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains plugins that should be executed when an interface
     * is closed.
     */
    private val interfaceClosePlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains command plugins. The pair has the privilege power
     * required to use the command on the left, and the plugin on the right.
     *
     * The privilege power left value can be set to null, which means anyone
     * can use the command.
     */
    private val commandPlugins = hashMapOf<String, Pair<String?, Plugin.() -> Unit>>()

    /**
     * A map of button click plugins. The key is a shifted value of the parent
     * and child id.
     */
    private val buttonPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map of equipment option plugins.
     */
    private val equipmentOptionPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map of plugins that contain plugins that should execute when equipping
     * items from a certain equipment slot.
     */
    private val equipSlotPlugins: Multimap<Int, Plugin.() -> Unit> = HashMultimap.create()

    /**
     * A map of plugins that can stop an item from being equipped.
     */
    private val equipItemRequirementPlugins = Int2ObjectOpenHashMap<Plugin.() -> Boolean>()

    /**
     * A map of plugins that are executed when a player equips an item.
     */
    private val equipItemPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map of plugins that are executed when a player un-equips an item.
     */
    private val unequipItemPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A plugin that executes when a player levels up a skill.
     */
    private var skillLevelUps: (Plugin.() -> Unit)? = null

    private val componentItemSwapPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    private val componentToComponentItemSwapPlugins = Long2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains any plugin that will be executed upon entering a new
     * region. The key is the region id and the value is a list of plugins
     * that will execute upon entering the region.
     */
    private val enterRegionPlugins = Int2ObjectOpenHashMap<MutableList<Plugin.() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed upon leaving a region.
     * The key is the region id and the value is a list of plugins that will execute
     * upon leaving the region.
     */
    private val exitRegionPlugins = Int2ObjectOpenHashMap<MutableList<Plugin.() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed upon entering a new
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val enterChunkPlugins = Int2ObjectOpenHashMap<MutableList<Plugin.() -> Unit>>()

    /**
     * A map that contains any plugin that will be executed when leaving a
     * [gg.rsmod.game.model.region.Chunk]. The key is the chunk id which can be
     * calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    private val exitChunkPlugins = Int2ObjectOpenHashMap<MutableList<Plugin.() -> Unit>>()

    /**
     * A map that contains items and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val itemPlugins = Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Plugin.() -> Unit>>()

    /**
     * A map that contains ground items and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val groundItemPlugins = Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Plugin.() -> Unit>>()

    /**
     * A map of plugins that check if an item with the associated key, can be
     * dropped on the floor.
     */
    private val canDropItemPlugins = Int2ObjectOpenHashMap<Plugin.() -> Boolean>()

    /**
     * A map that contains objects and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val objectPlugins = Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Plugin.() -> Unit>>()

    /**
     * A map that contains items and any objects that they may be used on, and it's
     * respective plugin logic.
     */
    private val itemOnObjectPlugins = Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Plugin.() -> Unit>>()

    /**
     * A map that contains item on item plugins.
     *
     * Key: (itemId1 << 16) | itemId2
     * Value: plugin
     */
    private val itemOnItemPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains magic spell on item plugins.
     *
     * Key: (fromComponentHash << 32) | toComponentHash
     * Value: plugin
     */
    private val spellOnItemPlugins = Long2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map that contains npcs and any associated menu-click and its respective
     * plugin logic, if any (would not be in the map if it doesn't have a plugin).
     */
    private val npcPlugins = Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Plugin.() -> Unit>>()

    /**
     * A map that contains npc ids as the key and their interaction distance as
     * the value. If map does not contain an npc, it will have the default interaction
     */
    private val npcInteractionDistancePlugins = Int2IntOpenHashMap()

    /**
     * A map that contains object ids as the key and their interaction distance as
     * the value. If map does not contain an object, it will have the default interaction
     */
    private val objInteractionDistancePlugins = Int2IntOpenHashMap()

    /**
     * A list of plugins that will be invoked when a ground item is picked up
     * by a player.
     */
    private val globalGroundItemPickUp = mutableListOf<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be invoked when a player hits 0 hp.
     */
    private val playerPreDeathPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be invoked when a player dies and is teleported
     * back to the respawn location (after death animation played out).
     */
    private val playerDeathPlugins = mutableListOf<Plugin.() -> Unit>()

    /**
     * A map of plugins that are invoked when a player interaction option is executed
     */
    private val playerOptionPlugins = hashMapOf<String, Plugin.() -> Unit>()

    /**
     * A list of plugins that will be invoked when an npc hits 0 hp.
     */
    private val npcPreDeathPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A list of plugins that will be invoked when an npc dies
     * and is de-registered from the world.
     */
    private val npcDeathPlugins = Int2ObjectOpenHashMap<Plugin.() -> Unit>()

    /**
     * A map of plugins that occur when an [Event] is triggered.
     */
    private val eventPlugins = Object2ObjectOpenHashMap<Class<out Event>, MutableList<Plugin.(Event) -> Unit>>()

    /**
     * The int value is calculated via [gg.rsmod.game.model.region.ChunkCoords.hashCode].
     */
    internal val multiCombatChunks = IntOpenHashSet()

    /**
     * The int value is calculated via [gg.rsmod.game.model.Tile.regionId].
     */
    internal val multiCombatRegions = IntOpenHashSet()

    /**
     * Temporarily holds all npc spawns set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val npcSpawns = mutableListOf<Npc>()

    /**
     * Temporarily holds all object spawns set from plugins for this [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val objSpawns = mutableListOf<DynamicObject>()

    /**
     * Temporarily holds all ground item spawns set from plugins for this
     * [PluginRepository].
     * This is then passed onto the [World] and is cleared.
     */
    internal val itemSpawns = mutableListOf<GroundItem>()

    /**
     * A map of [NpcCombatDef]s that have been set by [KotlinPlugin]s.
     */
    internal val npcCombatDefs = Int2ObjectOpenHashMap<NpcCombatDef>()

    /**
     * Holds all valid shops set from plugins for this [PluginRepository].
     */
    internal val shops = Object2ObjectOpenHashMap<String, Shop>()

    /**
     * A list of [Service]s that have been requested for loading by a [KotlinPlugin].
     */
    internal val services = mutableListOf<Service>()

    /**
     * Holds all container keys set from plugins for this [PluginRepository].
     */
    val containerKeys = ObjectOpenHashSet<ContainerKey>().apply {
        add(INVENTORY_KEY)
        add(EQUIPMENT_KEY)
        add(BANK_KEY)
    }

    /**
     * Initiates and populates all our plugins.
     */
    fun init(server: Server, world: World, jarPluginsDirectory: String) {
        loadPlugins(server, jarPluginsDirectory)
        loadServices(server, world)
        spawnEntities()
    }

    /**
     * Locate and load all [KotlinPlugin]s.
     */
    private fun loadPlugins(server: Server, jarPluginsDirectory: String) {
        scanPackageForPlugins(server, world)
        scanJarDirectoryForPlugins(server, world, Paths.get(jarPluginsDirectory))
    }

    /**
     * Scan our local package to find any and all [KotlinPlugin]s.
     */
    private fun scanPackageForPlugins(server: Server, world: World) {
        ClassGraph().enableAllInfo().whitelistModules().scan().use { result ->
            val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
            plugins.forEach { p ->
                val pluginClass = p.loadClass(KotlinPlugin::class.java)
                val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java, Server::class.java)
                constructor.newInstance(this, world, server)
            }
        }
    }

    /**
     * Scan directory for any JAR file which may contain plugins.
     */
    private fun scanJarDirectoryForPlugins(server: Server, world: World, directory: Path) {
        if (Files.exists(directory)) {
            Files.walk(directory).forEach { path ->
                if (!path.fileName.toString().endsWith(".jar")) {
                    return@forEach
                }
                scanJarForPlugins(server, world, path)
            }
        }
    }

    /**
     * Scan JAR located in [path] for any and all valid [KotlinPlugin]s and
     * initialise them.
     */
    private fun scanJarForPlugins(server: Server, world: World, path: Path) {
        val urls = arrayOf(path.toFile().toURI().toURL())
        val classLoader = URLClassLoader(urls, PluginRepository::class.java.classLoader)

        ClassGraph().ignoreParentClassLoaders().addClassLoader(classLoader).enableAllInfo().scan().use { result ->
            val plugins = result.getSubclasses(KotlinPlugin::class.java.name).directOnly()
            plugins.forEach { p ->
                val pluginClass = p.loadClass(KotlinPlugin::class.java)
                val constructor = pluginClass.getConstructor(PluginRepository::class.java, World::class.java, Server::class.java)
                constructor.newInstance(this, world, server)
            }
        }
    }

    /**
     * Load and initialise [Service]s given to us by [KotlinPlugin]s.
     */
    private fun loadServices(server: Server, world: World) {
        services.forEach { service ->
            service.init(server, world, ServerProperties())
            world.services.add(service)
        }

        services.forEach { service ->
            service.postLoad(server, world)
        }
    }

    /**
     * Spawn any and all [gg.rsmod.game.model.entity.Entity]s given to us by
     * [KotlinPlugin]s.
     */
    private fun spawnEntities() {
        npcSpawns.forEach { npc -> world.spawn(npc) }
        objSpawns.forEach { obj -> world.spawn(obj) }
        itemSpawns.forEach { item -> world.spawn(item) }
    }

    /**
     * Gracefully terminate this repository.
     */
    fun terminate() {
        npcSpawns.forEach { npc ->
            if (npc.isSpawned()) {
                world.remove(npc)
            }
        }

        objSpawns.forEach { obj ->
            if (obj.isSpawned(world)) {
                world.remove(obj)
            }
        }

        itemSpawns.forEach { item ->
            if (item.isSpawned(world)) {
                world.remove(item)
            }
        }

        world.services.removeAll(services)
    }

    /**
     * Get the total amount of plugins loaded from the plugins path.
     */
    fun getPluginCount(): Int = pluginCount

    fun getNpcInteractionDistance(npc: Int): Int? = npcInteractionDistancePlugins.getOrDefault(npc, null)

    fun getObjInteractionDistance(obj: Int): Int? = objInteractionDistancePlugins.getOrDefault(obj, null)

    fun bindWorldInit(plugin: Plugin.() -> Unit) {
        worldInitPlugins.add(plugin)
    }

    fun executeWorldInit(world: World) {
        worldInitPlugins.forEach { logic -> world.executePlugin(world, logic) }
    }

    fun bindCombat(plugin: Plugin.() -> Unit) {
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

    fun bindNpcCombat(npc: Int, plugin: Plugin.() -> Unit) {
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

    fun bindPlayerPreDeath(plugin: Plugin.() -> Unit) {
        playerPreDeathPlugins.add(plugin)
    }

    fun executePlayerPreDeath(p: Player) {
        playerPreDeathPlugins.forEach { plugin -> p.executePlugin(plugin) }
    }

    fun bindPlayerOption(option: String, plugin: Plugin.() -> Unit) {
        playerOptionPlugins[option] = plugin
    }

    fun executePlayerOption(player: Player, option: String): Boolean {
        val logic = playerOptionPlugins[option] ?: return false
        player.executePlugin(logic)
        return true
    }

    fun bindPlayerDeath(plugin: Plugin.() -> Unit) {
        playerDeathPlugins.add(plugin)
    }

    fun executePlayerDeath(p: Player) {
        playerDeathPlugins.forEach { plugin -> p.executePlugin(plugin) }
    }

    fun bindNpcPreDeath(npc: Int, plugin: Plugin.() -> Unit) {
        npcPreDeathPlugins[npc] = plugin
    }

    fun executeNpcPreDeath(npc: Npc) {
        npcPreDeathPlugins[npc.id]?.let { plugin ->
            npc.executePlugin(plugin)
        }
    }

    fun bindNpcDeath(npc: Int, plugin: Plugin.() -> Unit) {
        npcDeathPlugins[npc] = plugin
    }

    fun executeNpcDeath(npc: Npc) {
        npcDeathPlugins[npc.id]?.let { plugin ->
            npc.executePlugin(plugin)
        }
    }

    fun bindSpellOnNpc(parent: Int, child: Int, plugin: Plugin.() -> Unit) {
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

    fun bindWindowStatus(plugin: Plugin.() -> Unit) {
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

    fun bindModalClose(plugin: Plugin.() -> Unit) {
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

    fun setMenuOpenedCheck(plugin: Plugin.() -> Boolean) {
        if (isMenuOpenedPlugin != null) {
            logger.error("\"Menu Opened\" is already bound to a plugin")
            throw IllegalStateException("\"Menu Opened\" is already bound to a plugin")
        }
        isMenuOpenedPlugin = plugin
    }

    fun isMenuOpened(p: Player): Boolean = if (isMenuOpenedPlugin != null) p.executePlugin(isMenuOpenedPlugin!!) else false

    fun <T : Event> bindEvent(event: Class<T>, plugin: Plugin.(Event) -> Unit) {
        val plugins = eventPlugins[event]
        if (plugins != null) {
            plugins.add(plugin)
        } else {
            val newList = ObjectArrayList<Plugin.(Event) -> Unit>(1)
            newList.add(plugin)
            eventPlugins[event] = newList
        }

        pluginCount++
    }

    fun <T : Event> executeEvent(p: Pawn, event: T) {
        eventPlugins[event::class.java]?.forEach { plugin ->
            p.executePlugin {
                plugin.invoke(this, event)
            }
        }
    }

    fun bindLogin(plugin: Plugin.() -> Unit) {
        loginPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogin(p: Player) {
        loginPlugins.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindLogout(plugin: Plugin.() -> Unit) {
        logoutPlugins.add(plugin)
        pluginCount++
    }

    fun executeLogout(p: Player) {
        logoutPlugins.forEach { logic -> p.executePlugin(logic) }
    }

    fun bindComponentItemSwap(interfaceId: Int, component: Int, plugin: Plugin.() -> Unit) {
        val hash = (interfaceId shl 16) or component
        componentItemSwapPlugins[hash] = plugin
    }

    fun executeComponentItemSwap(p: Player, interfaceId: Int, component: Int): Boolean {
        val hash = (interfaceId shl 16) or component
        val plugin = componentItemSwapPlugins[hash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindComponentToComponentItemSwap(srcInterfaceId: Int, srcComponent: Int, dstInterfaceId: Int, dstComponent: Int, plugin: Plugin.() -> Unit) {
        val srcHash = (srcInterfaceId shl 16) or srcComponent
        val dstHash = (dstInterfaceId shl 16) or dstComponent
        val combinedHash = ((srcHash shl 32) or dstHash).toLong()
        componentToComponentItemSwapPlugins[combinedHash] = plugin
    }

    fun executeComponentToComponentItemSwap(p: Player, srcInterfaceId: Int, srcComponent: Int, dstInterfaceId: Int, dstComponent: Int): Boolean {
        val srcHash = (srcInterfaceId shl 16) or srcComponent
        val dstHash = (dstInterfaceId shl 16) or dstComponent
        val combinedHash = ((srcHash shl 32) or dstHash).toLong()
        val plugin = componentToComponentItemSwapPlugins[combinedHash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindGlobalNpcSpawn(plugin: Plugin.() -> Unit) {
        globalNpcSpawnPlugins.add(plugin)
        pluginCount++
    }

    fun bindNpcSpawn(npc: Int, plugin: Plugin.() -> Unit) {
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

    fun bindTimer(key: TimerKey, plugin: Plugin.() -> Unit) {
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

    fun bindInterfaceOpen(interfaceId: Int, plugin: Plugin.() -> Unit) {
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

    fun bindInterfaceClose(interfaceId: Int, plugin: Plugin.() -> Unit) {
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

    fun bindCommand(command: String, powerRequired: String? = null, plugin: Plugin.() -> Unit) {
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

    fun bindButton(parent: Int, child: Int, plugin: Plugin.() -> Unit) {
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

    fun bindEquipmentOption(item: Int, option: Int, plugin: Plugin.() -> Unit) {
        val hash = (item shl 16) or option
        if (equipmentOptionPlugins.containsKey(hash)) {
            logger.error(RuntimeException("Button hash already bound to a plugin: [item=$item, opt=$option]")) {}
            return
        }
        equipmentOptionPlugins[hash] = plugin
        pluginCount++
    }

    fun executeEquipmentOption(p: Player, item: Int, option: Int): Boolean {
        val hash = (item shl 16) or option
        val plugin = equipmentOptionPlugins[hash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindEquipSlot(equipSlot: Int, plugin: Plugin.() -> Unit) {
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

    fun bindEquipItemRequirement(item: Int, plugin: Plugin.() -> Boolean) {
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
            /*
             * Plugin returns true if the item can be equipped, false if it
             * should block the item from being equipped.
             */
            return p.executePlugin(plugin)
        }
        /*
         * Should always be able to wear items by default.
         */
        return true
    }

    fun bindEquipItem(item: Int, plugin: Plugin.() -> Unit) {
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

    fun bindUnequipItem(item: Int, plugin: Plugin.() -> Unit) {
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

    fun bindRegionEnter(regionId: Int, plugin: Plugin.() -> Unit) {
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

    fun bindRegionExit(regionId: Int, plugin: Plugin.() -> Unit) {
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

    fun bindChunkEnter(chunkHash: Int, plugin: Plugin.() -> Unit) {
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

    fun bindChunkExit(chunkHash: Int, plugin: Plugin.() -> Unit) {
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

    fun bindItem(id: Int, opt: Int, plugin: Plugin.() -> Unit) {
        val optMap = itemPlugins[id] ?: Int2ObjectOpenHashMap(1)
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

    fun bindGroundItem(id: Int, opt: Int, plugin: Plugin.() -> Unit) {
        val optMap = groundItemPlugins[id] ?: Int2ObjectOpenHashMap(1)
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

    fun bindCanItemDrop(item: Int, plugin: Plugin.() -> Boolean) {
        if (canDropItemPlugins.containsKey(item)) {
            logger.error("Item already bound to a 'can-drop' plugin: $item")
            throw IllegalStateException("Item already bound to a 'can-drop' plugin: $item")
        }
        canDropItemPlugins[item] = plugin
    }

    fun canDropItem(p: Player, item: Int): Boolean {
        val plugin = canDropItemPlugins[item]
        if (plugin != null) {
            return p.executePlugin(plugin)
        }
        return true
    }

    fun bindItemOnObject(obj: Int, item: Int, lineOfSightDistance: Int = -1, plugin: Plugin.() -> Unit) {
        val plugins = itemOnObjectPlugins[item] ?: Int2ObjectOpenHashMap(1)
        if (plugins.containsKey(obj)) {
            val error = "Item is already bound to an object plugin: $item [obj=$obj]"
            logger.error(error)
            throw IllegalStateException(error)
        }

        if (lineOfSightDistance != -1) {
            objInteractionDistancePlugins[obj] = lineOfSightDistance
        }

        plugins[obj] = plugin
        itemOnObjectPlugins[item] = plugins
        pluginCount++
    }

    fun executeItemOnObject(p: Player, obj: Int, item: Int): Boolean {
        val plugins = itemOnObjectPlugins[item] ?: return false
        val logic = plugins[obj] ?: return false
        p.executePlugin(logic)
        return true
    }

    fun bindItemOnItem(item1: Int, item2: Int, plugin: Plugin.() -> Unit) {
        val max = Math.min(item1, item2)
        val min = Math.min(item1, item2)

        val hash = (max shl 16) or min

        if (itemOnItemPlugins.containsKey(hash)) {
            logger.error { "Item on Item pair is already bound to a plugin: [item1=$item1, item2=$item2]" }
            throw IllegalStateException("Item on Item pair is already bound to a plugin: [item1=$item1, item2=$item2]")
        }

        itemOnItemPlugins[hash] = plugin
        pluginCount++
    }

    fun executeItemOnItem(p: Player, item1: Int, item2: Int): Boolean {
        val max = Math.min(item1, item2)
        val min = Math.min(item1, item2)

        val hash = (max shl 16) or min
        val plugin = itemOnItemPlugins[hash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindSpellOnItem(fromComponentHash: Int, toComponentHash: Int, plugin: Plugin.() -> Unit) {
        val hash: Long = (fromComponentHash.toLong() shl 32) or toComponentHash.toLong()
        if (spellOnItemPlugins.containsKey(hash)) {
            val exception = RuntimeException("Spell on item already bound to a plugin: from=[${fromComponentHash shr 16}, ${fromComponentHash or 0xFFFF}], to=[${toComponentHash shr 16}, ${toComponentHash or 0xFFFF}]")
            logger.error(exception) {}
            throw exception
        }
        spellOnItemPlugins[hash] = plugin
        pluginCount++
    }

    fun executeSpellOnItem(p: Player, fromComponentHash: Int, toComponentHash: Int): Boolean {
        val hash: Long = (fromComponentHash.toLong() shl 32) or toComponentHash.toLong()
        val plugin = spellOnItemPlugins[hash] ?: return false
        p.executePlugin(plugin)
        return true
    }

    fun bindObject(obj: Int, opt: Int, lineOfSightDistance: Int = -1, plugin: Plugin.() -> Unit) {
        val optMap = objectPlugins[obj] ?: Int2ObjectOpenHashMap(1)
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

    fun bindNpc(npc: Int, opt: Int, lineOfSightDistance: Int = -1, plugin: Plugin.() -> Unit) {
        val optMap = npcPlugins[npc] ?: Int2ObjectOpenHashMap(1)
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

    fun bindGlobalGroundItemPickUp(plugin: Plugin.() -> Unit) {
        globalGroundItemPickUp.add(plugin)
    }

    fun executeGlobalGroundItemPickUp(p: Player) {
        globalGroundItemPickUp.forEach { plugin ->
            p.executePlugin(plugin)
        }
    }

    companion object : KLogging()
}