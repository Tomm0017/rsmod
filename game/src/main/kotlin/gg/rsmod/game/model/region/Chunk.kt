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

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(val coordinates: RegionCoordinates, heights: Int) {

    companion object {
        /**
         * The size of a chunk, in tiles.
         */
        const val CHUNK_SIZE = 8
    }

    private val matrices = CollisionMatrix.createMatrices(heights, CHUNK_SIZE, CHUNK_SIZE)

    private val entities: Multimap<Tile, Entity> = HashMultimap.create()

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun getMatrices(): Array<CollisionMatrix> = matrices

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val matrix = matrices[tile.height]
        return !matrix.isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)
    }

    fun isBlocked(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val matrix = matrices[tile.height]
        return matrix.isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)
    }

    fun addEntity(world: World, entity: Entity) {
        entities.put(entity.tile, entity)
        world.collision.submitUpdate(entity, CollisionUpdate.Type.ADDING)
    }

    fun contains(tile: Tile): Boolean {
        return coordinates == tile.toRegionCoordinates()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> {
        return entities.values().filter { it.getType() in types } as List<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> {
        return entities.get(tile).filter { it.getType() in types } as List<T>
    }
}