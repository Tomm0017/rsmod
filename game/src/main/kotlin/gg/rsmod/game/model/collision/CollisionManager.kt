package gg.rsmod.game.model.collision

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.region.Chunk

class CollisionManager(val world: World) {

    companion object {
        const val BLOCKED_TILE = 0x1
        const val BRIDGE_TILE = 0x2
    }

    fun submit(obj: GameObject, updateType: CollisionUpdate.Type) {
        val builder = CollisionUpdate.Builder()
        builder.setType(updateType)
        builder.putObject(world.definitions, obj)
        apply(builder.build())
    }

    fun canTraverse(tile: Tile, direction: Direction, type: EntityType): Boolean {
        val next = tile.step(direction)
        var chunk = world.chunks.getForTile(next)
        val projectile = type.isProjectile()

        if (!chunk.canTraverse(next, direction, projectile)) {
            return false
        }

        if (direction.isDiagonal()) {
            direction.getDiagonalComponents().forEach { other ->
                val otherNext = tile.step(other)

                if (!chunk.contains(otherNext)) {
                    chunk = world.chunks.getForTile(otherNext)
                }

                if (!chunk.canTraverse(otherNext, other, projectile)) {
                    return false
                }
            }
        }

        return true
    }

    private fun flag(type: CollisionUpdate.Type, matrix: CollisionMatrix, localX: Int, localY: Int, flag: CollisionFlag) {
        if (type === CollisionUpdate.Type.ADD) {
            matrix.addFlag(localX, localY, flag)
        } else {
            matrix.removeFlag(localX, localY, flag)
        }
    }

    fun apply(update: CollisionUpdate) {
        var chunk: Chunk? = null

        val type = update.type
        val map = update.flags.asMap()

        for (entry in map.entries) {
            val tile = entry.key

            if (chunk == null || !chunk.contains(tile)) {
                chunk = world.chunks.getForTile(tile)
            }

            val localX = tile.x % Chunk.CHUNK_SIZE
            val localZ = tile.z % Chunk.CHUNK_SIZE

            val matrix = chunk.getMatrix(tile.height)
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