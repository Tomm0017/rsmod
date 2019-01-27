package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimplePathFindingStrategy(override val world: World) : PathFindingStrategy(world) {

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end

        val projectile = request.projectilePath
        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val targetWidth = request.targetWidth
        val targetLength = request.targetLength
        val target = getTargetTiles(request).sortedBy { it.getDelta(start) }.first()

        val path = ArrayDeque<Tile>()
        path.add(start)

        if (areBordering(path.peekLast(), sourceWidth - 1, end, targetWidth - 1)
                && world.collision.raycast(path.peekLast(), end, projectile)
                && !areDiagonal(path.peekLast(), sourceWidth - 1, end, targetWidth - 1)) {
            return Route(path, success = true, tail = path.peekLast())
        }

        val optimalRoute = raycastPath(world.collision, start, target, sourceWidth, sourceLength, targetWidth, projectile)
        path.addAll(optimalRoute.path)

        println("optimal=$optimalRoute")

        if (!optimalRoute.success) {
            while (true) {
                if (areBordering(path.peekLast(), sourceWidth - 1, target, targetWidth - 1)) {
                    break
                }
                val verticalRoute = raycastPath(world.collision, path.peekLast(), Tile(path.peekLast().x, target.z, end.height), sourceWidth, sourceLength, targetLength, projectile)
                path.addAll(verticalRoute.path)

                if (areBordering(path.peekLast(), sourceLength - 1, target, targetLength - 1)) {
                    break
                }
                val horizontalRoute = raycastPath(world.collision, path.peekLast(), Tile(target.x, path.peekLast().z, start.height), sourceWidth, sourceLength, targetWidth, projectile)
                path.addAll(horizontalRoute.path)

                if (horizontalRoute.path.isEmpty()) {
                    break
                }
            }
        }

        val canReach = areBordering(path.peekLast(), sourceWidth - 1, end, targetWidth - 1)
                && world.collision.raycast(path.peekLast(), end, projectile)
                && !areDiagonal(path.peekLast(), sourceWidth - 1, end, targetWidth - 1)
        return Route(path, success = canReach, tail = path.peekLast())
    }

    private fun getTargetTiles(request: PathRequest): Set<Tile> {
        val end = request.end
        val targetWidth = request.targetWidth
        val targetLength = request.targetLength
        val touchRadius = request.touchRadius
        val targetTiles = hashSetOf<Tile>()

        if (targetWidth > 0 || targetLength > 0) {
            for (x in -1..targetWidth) {
                for (z in -1..targetLength) {
                    val tile = end.transform(x, z)
                    if (!request.validateBorder.invoke(tile)) {
                        continue
                    }
                    targetTiles.add(tile)
                }
            }
        } else {
            targetTiles.add(end)
        }

        if (touchRadius > 1) {
            for (x in -touchRadius..touchRadius) {
                for (z in -touchRadius..touchRadius) {
                    if (x in 0 until targetWidth && z in 0 until targetLength) {
                        continue
                    }
                    val tile = end.transform(x, z)
                    targetTiles.add(tile)
                }
            }
        }

        return targetTiles
    }

    private fun raycastPath(collision: CollisionManager, start: Tile, target: Tile, sourceWidth: Int, sourceLength: Int,
                            targetSize: Int, projectile: Boolean): Route {
        check(start.height == target.height) { "Tiles must be on the same height level." }

        val path = ArrayDeque<Tile>()

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

        var success = false
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

            val tile = Tile(x0, y0, start.height)
            val dir = Direction.between(old, tile)
            if (!canTraverse(collision, old, sourceWidth, sourceLength, dir, projectile)) {
                success = false
                break
            }
            old = tile
            path.add(old)
            success = true

            if (areBordering(tile, sourceWidth - 1, target, targetSize - 1)) {
                break
            }
        }

        return Route(path, success, if (path.isNotEmpty()) path.peekLast() else start)
    }

    private fun canTraverse(collision: CollisionManager, tile: Tile, width: Int, length: Int, direction: Direction, projectile: Boolean): Boolean {
        for (x in 0 until width) {
            for (z in 0 until length) {
                val transform = tile.transform(x, z)
                if (!collision.canTraverse(transform, direction, projectile)
                        || !collision.canTraverse(transform.step(direction), direction.getOpposite(), projectile)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Checks to see if two AABB (axis-aligned bounding box) are bordering,
     * but not overlapping.
     */
    private fun areBordering(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean {
        val a = Pair(tile1, tile1.transform(size1, size1))
        val b = Pair(tile2, tile2.transform(size2, size2))

        if (b.first.x in a.first.x .. a.second.x && b.first.z in a.first.z .. a.second.z
                || b.second.x in a.first.x .. a.second.x && b.second.z in a.first.z .. a.second.z) {
            return false
        }

        if (b.first.x > a.second.x + 1) {
            return false
        }

        if (b.second.x < a.first.x - 1) {
            return false
        }

        if (b.first.z > a.second.z + 1) {
            return false
        }

        if (b.second.z < a.first.z - 1) {
            return false
        }
        return true
    }

    private fun areDiagonal(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean {
        val a = Pair(tile1, tile1.transform(size1, size1))
        val b = Pair(tile2, tile2.transform(size2, size2))

        /**
         * South-west diagonal tile.
         */
        if (a.first.x - 1 == b.second.x && a.first.z - 1 == b.second.z) {
            return true
        }

        /**
         * South-east diagonal tile.
         */
        if (a.second.x + 1 == b.second.x && a.first.z - 1 == b.second.z) {
            return true
        }

        /**
         * North-west diagonal tile.
         */
        if (a.first.x - 1 == b.second.x && a.second.z + 1 == b.second.z) {
            return true
        }

        /**
         * North-east diagonal tile.
         */
        if (a.second.x + 1 == b.second.x && a.second.z + 1 == b.second.z) {
            return true
        }

        return false
    }
}