package gg.rsmod.game.model.collision

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.region.Chunk


class CollisionManager(val world: World) {

    companion object {
        const val BLOCKED_TILE = 0x1
        const val BRIDGE_TILE = 0x2
    }

    fun applyCollision(obj: GameObject, updateType: CollisionUpdate.Type) {
        val builder = CollisionUpdate.Builder()
        builder.setType(updateType)
        builder.putObject(world.definitions, obj)
        applyUpdate(builder.build())
    }

    fun isBlocked(tile: Tile, direction: Direction, projectile: Boolean): Boolean = world.chunks.getForTile(tile).isBlocked(tile, direction, projectile)

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val chunk = world.chunks.getForTile(tile)

        if (chunk.isBlocked(tile, direction, projectile)) {
            return false
        }

        if (direction.isDiagonal()) {
            direction.getDiagonalComponents().forEach { other ->
                val next = tile.step(other)
                val otherChunk = world.chunks.getForTile(next)
                if (otherChunk.isBlocked(next, other.getOpposite(), projectile)) {
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

    fun applyUpdate(update: CollisionUpdate) {
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

    /**
     * Casts a line using Bresenham's Line Algorithm with point A [start] and
     * point B [target] being its two points and makes sure that there's no
     * collision flag that can block movement from and to both points.
     *
     * @param projectile
     * Projectiles have a higher tolerance for certain objects when the object's
     * metadata explicitly allows them to.
     */
    fun raycast(start: Tile, target: Tile, projectile: Boolean): Boolean {
        check(start.height == target.height) { "Tiles must be on the same height level." }

        var x0 = start.x
        var y0 = start.z
        val x1 = target.x
        val y1 = target.z

        val dx = Math.abs(x1 - x0)
        val dy = Math.abs(y1 - y0)

        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1

        var err = dx - dy
        var err2: Int

        var old = Tile(x0, y0, start.height)

        while (x0 != x1 || y0 != y1) {
            err2 = err shl 1

            if (err2 > -dy) {
                err -= dy
                x0 += sx
            }

            if (err2 < dx) {
                err += dx
                y0 += sy
            }

            val tile = Tile(x0, y0, old.height)
            val dir = Direction.between(old, tile)
            if (!canTraverse(old, dir, projectile) || !canTraverse(tile, dir.getOpposite(), projectile)) {
                return false
            }
            old = tile
        }

        return true
    }
}