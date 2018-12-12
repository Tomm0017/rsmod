package gg.rsmod.game.model.collision

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.EntityType
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.RegionCoordinates

class CollisionManager(val world: World) {

    companion object {
        const val BLOCKED_TILE = 0x1

        const val BRIDGE_TILE = 0x2
    }

    private val blocked: Multimap<RegionCoordinates, Tile> = HashMultimap.create()

    private val bridges = hashSetOf<Tile>()

    fun block(tile: Tile) {
        blocked.put(tile.toRegionCoordinates(), tile)
    }

    fun markBridged(tile: Tile) {
        bridges.add(tile)
    }

    fun submitUpdate(entity: Entity, updateType: CollisionUpdate.Type) {
        if (entity is GameObject) {
            val builder = CollisionUpdate.Builder()
            builder.setType(updateType)
            builder.putObject(world.definitions, entity)
            apply(builder.build())
        }
    }

    fun canTraverse(tile: Tile, type: EntityType, direction: Direction): Boolean {
        val next = tile.step(1, direction)
        var chunk = world.regions.getChunkForTile(next)
        val projectile = type == EntityType.PROJECTILE

        if (!chunk.canTraverse(next, direction, projectile)) {
            return false
        }

        if (direction.isDiagonal()) {
            direction.getDiagonalComponents().forEach { other ->
                val otherNext = tile.step(1, other)

                if (!chunk.contains(otherNext)) {
                    chunk = world.regions.getChunkForTile(otherNext)
                }

                if (!chunk.canTraverse(otherNext, other, projectile)) {
                    return false
                }
            }
        }

        return true
    }

    fun build(rebuilding: Boolean) {
        if (rebuilding) {
            for (region in world.regions.getChunks()) {
                for (matrix in region.getMatrices()) {
                    matrix.reset()
                }
            }
        }

        world.regions.getChunks().forEach { chunk ->
            val builder = CollisionUpdate.Builder()
            builder.setType(CollisionUpdate.Type.ADDING)

            blocked.get(chunk.coordinates).forEach { tile ->
                val x = tile.x
                val z = tile.z
                var height = tile.height

                if (bridges.contains(Tile(x, z, 1))) {
                    height--
                }

                if (height >= 0) {
                    builder.putTile(Tile(x, z, height), false, *Direction.NESW)
                }
            }

            apply(builder.build())

            val objects = CollisionUpdate.Builder()
            objects.setType(CollisionUpdate.Type.ADDING)

            chunk.getEntities<GameObject>(EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT)
                    .forEach { entity -> objects.putObject(world.definitions, entity) }

            apply(objects.build())
            return@forEach
        }
    }

    private fun flag(type: CollisionUpdate.Type, matrix: CollisionMatrix, localX: Int, localY: Int, flag: CollisionFlag) {
        if (type === CollisionUpdate.Type.ADDING) {
            matrix.addFlag(localX, localY, flag)
        } else {
            matrix.removeFlag(localX, localY, flag)
        }
    }

    private fun apply(update: CollisionUpdate) {
        var chunk: Chunk? = null

        val type = update.type
        val map = update.flags.asMap()

        for (entry in map.entries) {
            val tile = entry.key

            var height = tile.height
            if (bridges.contains(Tile(tile.x, tile.z, 1))) {
                if (--height < 0) {
                    continue
                }
            }

            if (chunk == null || !chunk.contains(tile)) {
                chunk = world.regions.getChunkForTile(tile)
            }

            val localX = tile.x % Chunk.CHUNK_SIZE
            val localZ = tile.z % Chunk.CHUNK_SIZE

            val matrix = chunk.getMatrix(height)
            val pawns = CollisionFlag.pawnFlags()
            val projectiles = CollisionFlag.projectileFlags()

            for (flag in entry.value) {
                val direction = flag.direction
                if (direction === Direction.NONE) {
                    continue
                }

                val orientation = direction.value
                if (flag.impenetrable) {
                    flag(type, matrix, localX, localZ, projectiles[orientation])
                }

                flag(type, matrix, localX, localZ, pawns[orientation])
            }
        }
    }
}