package gg.rsmod.game.model.region

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.message.impl.EntityGroupMessage
import gg.rsmod.game.message.impl.SetChunkToRegionOffset
import gg.rsmod.game.message.impl.SpawnEntityGroupsMessage
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.update.EntityUpdate
import gg.rsmod.game.model.region.update.EntityUpdateType
import gg.rsmod.game.model.region.update.ObjectRemoveUpdate
import gg.rsmod.game.model.region.update.ObjectSpawnUpdate
import gg.rsmod.game.service.GameService

/**
 * Represents an 8x8 tile in the game map.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(private val coords: ChunkCoords, private val heights: Int) {

    companion object {
        /**
         * The size of a chunk, in tiles.
         */
        const val CHUNK_SIZE = 8

        /**
         * The size of the viewport a [gg.rsmod.game.model.entity.Player] can
         * 'see' at a time, in tiles.
         */
        const val MAX_VIEWPORT = CHUNK_SIZE * 13

        /**
         * The amount of [Chunk]s that can be viewed at a time by default.
         */
        const val CHUNK_VIEW_RADIUS = 3
    }

    /**
     * The collision matrices of 8x8 tiles in [heights] different height levels.
     */
    private val matrices = CollisionMatrix.createMatrices(heights, CHUNK_SIZE, CHUNK_SIZE)

    /**
     * The [Entity]s that are currently registered to the [Tile] key. This is
     * not used for [gg.rsmod.game.model.entity.Pawn], but rather [Entity]s
     * that do not regularly change [Tile]s.
     */
    private val entities: Multimap<Tile, Entity> = HashMultimap.create()

    private val updates = arrayListOf<EntityUpdate<*>>()

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun contains(tile: Tile): Boolean = coords == tile.toChunkCoords()

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val matrix = matrices[tile.height]
        return !matrix.isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)
    }

    fun addEntity(world: World, entity: Entity, tile: Tile) {
        world.collision.submit(entity, CollisionUpdate.Type.ADD)
        entities.put(tile, entity)

        val update = createUpdateFor(entity, spawn = true)
        if (update != null) {
            updates.add(update)
            sendUpdate(world, update)
        }
    }

    fun removeEntity(world: World, entity: Entity, tile: Tile) {
        world.collision.submit(entity, CollisionUpdate.Type.REMOVE)
        entities.remove(tile, entity)

        val update = createUpdateFor(entity, spawn = false)
        if (update != null) {
            if (entity.getType() == EntityType.STATIC_OBJECT) {
                updates.add(update)
            } else {
                updates.removeIf { it.entity == entity }
            }
            sendUpdate(world, update)
        }
    }

    fun getSurroundingCoords(chunkRadius: Int = CHUNK_VIEW_RADIUS): MutableSet<ChunkCoords> {
        val surrounding = hashSetOf<ChunkCoords>()

        val radius = chunkRadius - 1

        for (x in -radius .. radius) {
            for (z in -radius .. radius) {
                surrounding.add(ChunkCoords(coords.x + x, coords.z + z))
            }
        }

        return surrounding
    }

    private fun sendUpdate(world: World, update: EntityUpdate<*>) {
        getSurroundingCoords().forEach { coords ->
            val chunk = world.chunks.getOrCreate(coords, create = false) ?: return@forEach
            chunk.getEntities<Client>(EntityType.CLIENT).forEach { client ->
                val local = client.lastKnownRegionBase!!.toLocal(update.entity.tile)
                client.write(SetChunkToRegionOffset(local.x, local.z))
                client.write(update.toMessage())
            }
        }
    }

    fun sendUpdates(p: Player, gameService: GameService) {
        val messages = arrayListOf<EntityGroupMessage>()

        updates.forEach { update ->
            val message = EntityGroupMessage(update.type.id, update.toMessage())
            messages.add(message)
        }

        if (messages.isNotEmpty()) {
            val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
            p.write(SpawnEntityGroupsMessage(local.x, local.z, gameService.messageEncoders, gameService.messageStructures, *messages.toTypedArray()))
        }
    }

    private fun <T: Entity> createUpdateFor(entity: T, spawn: Boolean): EntityUpdate<*>? = when (entity.getType()) {
        EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT ->
            if (spawn) ObjectSpawnUpdate(EntityUpdateType.SPAWN_OBJECT, entity as GameObject)
            else ObjectRemoveUpdate(EntityUpdateType.REMOVE_OBJECT, entity as GameObject)
        else -> null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities.get(tile).filter { it.getType() in types } as List<T>
}