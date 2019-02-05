package gg.rsmod.game.model

import gg.rsmod.game.DevContext
import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.entity.*
import gg.rsmod.game.model.region.ChunkSet
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.plugin.PluginExecutor
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.service.Service
import gg.rsmod.game.service.game.EntityExamineService
import gg.rsmod.game.service.game.NpcStatsService
import gg.rsmod.game.service.xtea.XteaKeyService
import gg.rsmod.game.sync.block.UpdateBlockSet
import gg.rsmod.util.ServerProperties
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*

/**
 * The game world, which stores all the entities and nodes that the world
 * needs to keep track of.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class World(val server: Server, val gameContext: GameContext, val devContext: DevContext) {

    companion object {
        private val logger = LogManager.getLogger(World::class.java)
    }

    val players = PawnList<Player>(gameContext.playerLimit)

    val npcs = PawnList<Npc>(Short.MAX_VALUE.toInt())

    val collision = CollisionManager(this)

    val chunks = ChunkSet(this)

    /**
     * A collection of our [Service]s specified in our game [ServerProperties]
     * files.
     */
    val services = arrayListOf<Service>()

    /**
     * The [PluginExecutor] is responsible for executing plugins as requested by
     * the game.
     */
    val pluginExecutor = PluginExecutor()

    /**
     * The [Store] is responsible for handling the data in our cache.
     */
    lateinit var filestore: Store

    /**
     * The [DefinitionSet] that holds general filestore data.
     */
    val definitions = DefinitionSet()

    /**
     * The plugin repository that's responsible for storing all the plugins found.
     */
    val plugins = PluginRepository(this)

    /**
     * The [PrivilegeSet] that is attached to our game.
     */
    val privileges = PrivilegeSet()

    /**
     * A cached value for [gg.rsmod.game.service.xtea.XteaKeyService] since it
     * is used frequently and in performance critical code. This value is set
     * when [XteaKeyService.init] is called.
     */
    var xteaKeyService: XteaKeyService? = null

    /**
     * The [UpdateBlockSet] for players.
     */
    val playerUpdateBlocks = UpdateBlockSet()

    /**
     * The [UpdateBlockSet] for npcs.
     */
    val npcUpdateBlocks = UpdateBlockSet()

    /**
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    private val random: Random = Random()

    /**
     * The amount of game cycles that have gone by since the world was first
     * initialized. This can reset back to [0], if it's signalled to overflow
     * any time soon.
     */
    var currentCycle = 0

    fun register(p: Player): Boolean {
        val registered = players.add(p)
        if (registered) {
            p.lastIndex = p.index
            return true
        }
        return false
    }

    fun unregister(p: Player) {
        players.remove(p)
    }

    fun spawn(npc: Npc): Boolean {
        val added = npcs.add(npc)
        if (added) {
            var combatDef: NpcCombatDef? = null
            val statService = getService(NpcStatsService::class.java).orElse(null)
            if (statService != null) {
                combatDef = statService.get(npc.id)
            }

            npc.combatDef = combatDef ?: NpcCombatDef.DEFAULT

            /**
             * Execute npc spawn plugins.
             */
            plugins.executeNpcSpawn(npc)
        }
        return added
    }

    fun remove(npc: Npc) {
        npcs.remove(npc)
    }

    fun spawn(obj: GameObject) {
        val tile = obj.tile
        val chunk = chunks.getForTile(tile)

        val oldObj = chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull { it.type == obj.type }
        if (oldObj != null) {
            chunk.removeEntity(this, oldObj, tile)
        }

        chunk.addEntity(this, obj, tile)
    }

    fun remove(obj: GameObject) {
        val tile = obj.tile
        val chunk = chunks.getForTile(tile)

        chunk.removeEntity(this, obj, tile)
    }

    fun spawn(item: GroundItem) {
        val tile = item.tile
        val chunk = chunks.getForTile(tile)

        val def = definitions.get(ItemDef::class.java, item.item)

        if (def.isStackable()) {
            val oldItem = chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull { it.item == item.item && it.owner == item.owner }
            if (oldItem != null) {
                val oldAmount = oldItem.amount
                val newAmount = Math.min(Int.MAX_VALUE.toLong(), item.amount.toLong() + oldItem.amount.toLong()).toInt()
                oldItem.amount = newAmount
                chunk.updateGroundItem(this, item, oldAmount, newAmount)
                return
            }
        }

        chunk.addEntity(this, item, tile)
    }

    fun remove(item: GroundItem) {
        val tile = item.tile
        val chunk = chunks.getForTile(tile)

        chunk.removeEntity(this, item, tile)
    }

    fun spawn(projectile: Projectile) {
        val tile = projectile.tile
        val chunk = chunks.getForTile(tile)

        chunk.addEntity(this, projectile, tile)
    }

    fun spawn(sound: AreaSound) {
        val tile = sound.tile
        val chunk = chunks.getForTile(tile)

        chunk.addEntity(this, sound, tile)
    }

    fun isSpawned(obj: GameObject): Boolean = chunks.getForTile(obj.tile).getEntities<GameObject>(obj.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).contains(obj)

    fun isSpawned(item: GroundItem): Boolean = chunks.getForTile(item.tile).getEntities<GroundItem>(item.tile, EntityType.GROUND_ITEM).contains(item)

    fun getPlayerForName(username: String): Optional<Player> {
        for (i in 0 until players.capacity) {
            val player = players.get(i) ?: continue
            if (player.username == username) {
                return Optional.of(player)
            }
        }
        return Optional.empty()
    }

    fun random(boundInclusive: Int) = random.nextInt(boundInclusive + 1)

    fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start

    fun randomDouble(): Double = random.nextDouble()

    fun chance(chance: Int, probability: Int): Boolean = random.nextInt(chance + 1) <= probability

    fun percentChance(probability: Double): Boolean {
        check(probability in 0.0 .. 100.0) { "Chance must be within range of [0.0 - 100.0]" }
        return random.nextDouble() <= (probability / 100.0)
    }

    fun findRandomTileAround(centre: Tile, radius: Int, centreWidth: Int = 0, centreLength: Int = 0): Tile? {
        val tiles = arrayListOf<Tile>()
        for (x in -radius .. radius) {
            for (z in -radius .. radius) {
                if (x in 0 until centreWidth && z in 0 until centreLength) {
                    continue
                }
                tiles.add(centre.transform(x, z))
            }
        }
        val filtered = tiles.filter { tile -> !collision.isClipped(tile) }
        if (filtered.isNotEmpty()) {
            return filtered.random()
        }
        return null
    }

    fun executePlugin(plugin: Function1<Plugin, Unit>) {
        pluginExecutor.execute(this, plugin)
    }

    fun loadUpdateBlocks(blocksFile: File) {
        val properties = ServerProperties().loadYaml(blocksFile)

        if (properties.has("players")) {
            playerUpdateBlocks.load(properties.extract("players"))
        }

        if (properties.has("npcs")) {
            npcUpdateBlocks.load(properties.extract("npcs"))
        }
    }

    fun sendExamine(p: Player, id: Int, type: ExamineEntityType) {
        val service = getService(EntityExamineService::class.java).orElse(null)
        if (service != null) {
            val examine = when (type) {
                ExamineEntityType.ITEM -> service.getItem(id)
                ExamineEntityType.NPC -> service.getNpc(id)
                ExamineEntityType.OBJECT -> service.getObj(id)
            }
            val extension = if (devContext.debugExamines) " ($id)" else ""
            p.message(examine + extension)
        } else {
            logger.warn("No examine service found! Could not send examine message to player: ${p.username}.")
        }
    }

    /**
     * Gets the first service that can be found which meets the criteria of:
     *
     * When [searchSubclasses] is true: the service class must be assignable to the [type].
     * When [searchSubclasses] is false: the service class must be equal to the [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Service> getService(type: Class<out T>, searchSubclasses: Boolean = false): Optional<T> {
        if (searchSubclasses) {
            val service = services.firstOrNull { type.isAssignableFrom(it::class.java) }
            return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
        }
        val service = services.firstOrNull { it::class.java == type }
        return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
    }
}