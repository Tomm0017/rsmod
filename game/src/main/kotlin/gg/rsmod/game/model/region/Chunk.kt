package gg.rsmod.game.model.region

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.region.update.ChunkUpdateType

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

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun contains(tile: Tile): Boolean = coords == tile.toChunkCoords()

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val matrix = matrices[tile.height]
        return !matrix.isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)
    }

    fun addEntity(world: World, entity: Entity) {
        submit(world, entity, ChunkUpdateType.ADD)
        world.collision.submit(entity, CollisionUpdate.Type.ADD)
    }

    fun removeEntity(world: World, entity: Entity) {
        submit(world, entity, ChunkUpdateType.REMOVE)
        world.collision.submit(entity, CollisionUpdate.Type.REMOVE)
    }

    fun getSurrounding(chunkRadius: Int = CHUNK_VIEW_RADIUS): MutableSet<ChunkCoords> {
        val surrounding = hashSetOf<ChunkCoords>()

        for (x in -chunkRadius .. chunkRadius) {
            for (z in -chunkRadius .. chunkRadius) {
                surrounding.add(ChunkCoords(coords.x + x, coords.z + z))
            }
        }

        return surrounding
    }

    private fun submit(world: World, entity: Entity, updateType: ChunkUpdateType) {
        if (updateType == ChunkUpdateType.ADD) {
            entities.put(entity.tile, entity)
        } else if (updateType == ChunkUpdateType.REMOVE) {
            entities.remove(entity.tile, entity)
        } else {
            throw IllegalArgumentException("Unhandled update type: $updateType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities.get(tile).filter { it.getType() in types } as List<T>
}