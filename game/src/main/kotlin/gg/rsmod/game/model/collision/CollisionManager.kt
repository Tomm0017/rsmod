package gg.rsmod.game.model.collision

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.ChunkSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CollisionManager(val chunks: ChunkSet, val createChunksIfNeeded: Boolean = true) {

    fun isClipped(tile: Tile): Boolean = chunks.get(tile, createChunksIfNeeded)!!.isClipped(tile)

    fun isBlocked(tile: Tile, direction: Direction, projectile: Boolean): Boolean = chunks.get(tile, createChunksIfNeeded)!!.isBlocked(tile, direction, projectile)

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val chunk = chunks.get(tile, createChunksIfNeeded)!!

        if (chunk.isBlocked(tile, direction, projectile)) {
            return false
        }

        if (direction.isDiagonal()) {
            direction.getDiagonalComponents().forEach { other ->
                val diagonalTile = tile.step(other)
                val diagonalChunk = chunks.get(diagonalTile, createChunksIfNeeded)!!
                if (diagonalChunk.isBlocked(diagonalTile, other.getOpposite(), projectile)) {
                    return false
                }
            }
        }

        return true
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
        val height = start.height

        val dx = Math.abs(x1 - x0)
        val dy = Math.abs(y1 - y0)

        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1

        var err = dx - dy
        var err2: Int

        var prev = Tile(x0, y0, height)

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

            val next = Tile(x0, y0, height)
            val dir = Direction.between(prev, next)
            if (!canTraverse(prev, dir, projectile) || !canTraverse(next, dir.getOpposite(), projectile)) {
                return false
            }
            prev = next
        }

        return true
    }

    /**
     * Gets the shortest path using Bresenham's Line Algorithm from [start] to [target],
     * in tiles.
     */
    fun raycastTiles(start: Tile, target: Tile): Int {
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

        var tiles = 0

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
            tiles++
        }

        return tiles
    }

    private fun flag(type: CollisionUpdate.Type, matrix: CollisionMatrix, localX: Int, localY: Int, flag: CollisionFlag) {
        if (type == CollisionUpdate.Type.ADD) {
            matrix.addFlag(localX, localY, flag)
        } else {
            matrix.removeFlag(localX, localY, flag)
        }
    }

    fun applyCollision(definitions: DefinitionSet, obj: GameObject, updateType: CollisionUpdate.Type) {
        val builder = CollisionUpdate.Builder()
        builder.setType(updateType)
        builder.putObject(definitions, obj)
        applyUpdate(builder.build())
    }

    fun applyUpdate(update: CollisionUpdate) {
        var chunk: Chunk? = null

        val type = update.type
        val map = update.flags

        for (entry in map.entries) {
            val tile = entry.key

            if (chunk == null || !chunk.contains(tile)) {
                chunk = chunks.get(tile, createChunksIfNeeded)!!
            }

            val localX = tile.x % Chunk.CHUNK_SIZE
            val localZ = tile.z % Chunk.CHUNK_SIZE

            val matrix = chunk.getMatrix(tile.height)
            val pawns = CollisionFlag.pawnFlags()
            val projectiles = CollisionFlag.projectileFlags()

            for (flag in entry.value) {
                val direction = flag.direction
                if (direction == Direction.NONE) {
                    continue
                }

                val orientation = direction.orientationValue
                if (flag.impenetrable) {
                    flag(type, matrix, localX, localZ, projectiles[orientation])
                }

                flag(type, matrix, localX, localZ, pawns[orientation])
            }
        }
    }

    companion object {
        const val BLOCKED_TILE = 0x1
        const val BRIDGE_TILE = 0x2
        const val ROOF_TILE = 0x4
    }
}
