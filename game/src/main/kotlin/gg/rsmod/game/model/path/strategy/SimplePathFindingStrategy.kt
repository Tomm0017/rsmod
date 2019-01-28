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

    // TODO(Tom): redo this whole strategy (used for npcs). Fucking hate how
    // it is atm (jan 27 2019).

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end

        val projectile = request.projectilePath
        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val targetWidth = request.targetWidth
        val targetLength = request.targetLength

        val path = ArrayDeque<Tile>()
        var success = false

        var searchLimit = 2
        while (searchLimit-- > 0) {
            var tail = if (path.isNotEmpty()) path.peekLast() else start
            if (areBordering(tail, sourceWidth - 1, end, targetWidth - 1) && !areDiagonal(tail, sourceWidth - 1, end, targetWidth - 1)
                    && world.collision.raycast(tail, end, projectile)) {
                success = true
                break
            }

            var eastOrWest = if (tail.x < end.x) Direction.EAST else Direction.WEST
            var northOrSouth = if (tail.z < end.z) Direction.NORTH else Direction.SOUTH
            var overlapped = false

            if (overlap(tail, sourceWidth - 1, end, targetWidth - 1)) {
                eastOrWest = eastOrWest.getOpposite()
                northOrSouth = northOrSouth.getOpposite()
                overlapped = true
            }

            while ((!areCoordinatesInRange(tail.z, sourceLength - 1, end.z, targetLength - 1) || areDiagonal(tail, sourceLength - 1, end, targetLength - 1) || overlap(tail, sourceLength - 1, end, targetLength - 1))
                    && (overlapped || !overlap(tail.step(northOrSouth), sourceLength - 1, end, targetLength - 1))
                    && canTraverse(world.collision, tail, sourceWidth, sourceLength, northOrSouth, projectile)) {
                tail = tail.step(northOrSouth)
                path.add(tail)
            }

            while ((!areCoordinatesInRange(tail.x, sourceWidth - 1, end.x, targetWidth - 1) || areDiagonal(tail, sourceWidth - 1, end, targetWidth - 1) || overlap(tail, sourceWidth - 1, end, targetWidth - 1))
                    && (overlapped || !overlap(tail.step(eastOrWest), sourceWidth - 1, end, targetWidth - 1))
                    && canTraverse(world.collision, tail, sourceWidth, sourceLength, eastOrWest, projectile)) {
                tail = tail.step(eastOrWest)
                path.add(tail)
            }
        }

        return Route(path, success, tail = if (path.isNotEmpty()) path.peekLast() else start)
    }

    private fun canTraverse(collision: CollisionManager, tile: Tile, width: Int, length: Int, direction: Direction, projectile: Boolean): Boolean {
        for (x in 0 until width) {
            for (z in 0 until length) {
                val transform = tile.transform(x, z)
                if (!collision.canTraverse(transform, direction, projectile) || !collision.canTraverse(transform.step(direction), direction.getOpposite(), projectile)) {
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

    private fun areCoordinatesInRange(coord1: Int, size1: Int, coord2: Int, size2: Int): Boolean {
        val a = Pair(coord1, coord1 + size1)
        val b = Pair(coord2, coord2 + size2)

        if (a.second < b.first) {
            return false
        }

        if (a.first > b.second) {
            return false
        }

        return true
    }

    /**
     * Checks to see if two AABB (axis-aligned bounding box) overlap.
     */
    private fun overlap(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean {
        val a = Pair(tile1, tile1.transform(size1, size1))
        val b = Pair(tile2, tile2.transform(size2, size2))

        if (a.first.x > b.second.x || b.first.x > a.second.x) {
            return false
        }

        if (a.first.z > b.second.z || b.first.z > a.second.z) {
            return false
        }

        return true
    }
}