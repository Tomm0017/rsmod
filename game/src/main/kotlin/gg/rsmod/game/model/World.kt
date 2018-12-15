package gg.rsmod.game.model

import gg.rsmod.game.DevContext
import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.message.impl.RemoveObjectMessage
import gg.rsmod.game.message.impl.SetMapChunkMessage
import gg.rsmod.game.message.impl.SpawnObjectMessage
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.RegionSet
import gg.rsmod.game.plugin.PluginExecutor
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import net.runelite.cache.fs.Store
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * The game world, which stores all the entities and nodes that the world
 * needs to keep track of.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class World(val server: Server, val gameContext: GameContext, val devContext: DevContext) {

    val players = PawnList<Player>(gameContext.playerLimit)

    val npcs = PawnList<Npc>(Short.MAX_VALUE.toInt())

    val collision = CollisionManager(this)

    val regions = RegionSet(this)

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
    val plugins = PluginRepository()

    /**
     * The [PrivilegeSet] that is attached to our game.
     */
    val privileges = PrivilegeSet()

    /**
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    private val random: Random = ThreadLocalRandom.current()

    /**
     * The amount of game cycles that have gone by since the world was first
     * initialized. This can reset back to [0], if it's signalled to overflow
     * any time soon.
     */
    var currentCycle = 0

    fun register(p: Player): Boolean {
        val registered = players.add(p)
        if (registered) {
            p.attr.put(INDEX_ATTR, p.index)
            return true
        }
        return false
    }

    fun unregister(p: Player) {
        players.remove(p)
    }

    fun spawn(npc: Npc) {

    }

    fun remove(npc: Npc) {

    }

    fun spawn(obj: GameObject) {
        val tile = obj.tile

        val chunk = regions.getChunkForTile(tile)

        val oldObj = chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT)
                .firstOrNull { it.type == obj.type }
        if (oldObj != null) {
            chunk.removeEntity(this, oldObj)
        }

        chunk.addEntity(this, obj)

        players.forEach { player ->
            if (player.tile.isWithinRadius(obj.tile, 15)) {
                val cx = ((tile.x - player.lastKnownRegionBase!!.x) / 8) * 8
                val cz = ((tile.z - player.lastKnownRegionBase!!.z) / 8) * 8
                player.write(SetMapChunkMessage(cx, cz))
                player.write(SpawnObjectMessage(obj.id, obj.settings.toInt(), ((tile.x and 0x7) shl 4) or (tile.z and 0x7)))
            }
        }
    }

    fun remove(obj: GameObject) {
        val tile = obj.tile

        val chunk = regions.getChunkForTile(tile)

        chunk.removeEntity(this, obj)

        players.forEach { player ->
            if (player.tile.isWithinRadius(obj.tile, 15)) {
                val x = ((tile.x - player.lastKnownRegionBase!!.x) / 8) * 8
                val z = ((tile.z - player.lastKnownRegionBase!!.z) / 8) * 8
                player.write(SetMapChunkMessage(x, z))
                player.write(RemoveObjectMessage(obj.settings.toInt(), ((tile.x and 0x7) shl 4) or (tile.z and 0x7)))
            }
        }
    }

    fun showSpawns(p: Player) {
        val tile = p.tile
        for (x in -3..3) {
            for (z in -3..3) {
                val chunkTile = tile.transform(x * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE)
                val chunk = regions.getChunkNullable(chunkTile.toRegionCoordinates()) ?: continue

                chunk.getEntities<DynamicObject>(EntityType.DYNAMIC_OBJECT).forEach { obj ->
                    val cx = ((tile.x - p.lastKnownRegionBase!!.x) / 8) * 8
                    val cz = ((tile.z - p.lastKnownRegionBase!!.z) / 8) * 8
                    p.write(SetMapChunkMessage(cx, cz))
                    p.write(SpawnObjectMessage(obj.id, obj.settings.toInt(), ((tile.x and 0x7) shl 4) or (tile.z and 0x7)))
                }
            }
        }
    }

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

    /**
     * Gets the first service that can be found which meets the criteria of:
     *
     * When [searchSubclasses] is true: the service class must be assignable to the [type].
     * When [searchSubclasses] is false: the service class must be equal to the [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Service> getService(type: Class<out T>, searchSubclasses: Boolean): Optional<T> {
        if (searchSubclasses) {
            val service = services.firstOrNull { type.isAssignableFrom(it::class.java) }
            return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
        }
        val service = services.firstOrNull { it::class.java == type }
        return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
    }
}