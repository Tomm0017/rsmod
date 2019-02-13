package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import gg.rsmod.util.AabbUtil
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimplePathFindingStrategy(collision: CollisionManager) : PathFindingStrategy(collision) {

    // TODO(Tom): redo this whole strategy (used for npcs). Fucking hate how
    // it is atm (jan 27 2019).

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end

        val projectile = request.projectilePath
        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val targetWidth = Math.max(request.touchRadius, request.targetWidth)
        val targetLength = Math.max(request.touchRadius, request.targetLength)

        val path = ArrayDeque<Tile>()
        var success = false

        var searchLimit = 2
        while (searchLimit-- > 0) {
            var tail = if (path.isNotEmpty()) path.peekLast() else start
            if (areBordering(tail, sourceWidth, end, targetWidth) && !areDiagonal(tail, sourceWidth, end, targetWidth)
                    && collision.raycast(tail, end, projectile)) {
                success = true
                break
            }

            var eastOrWest = if (tail.x < end.x) Direction.EAST else Direction.WEST
            var northOrSouth = if (tail.z < end.z) Direction.NORTH else Direction.SOUTH
            var overlapped = false

            if (areOverlapping(tail, sourceWidth, end, targetWidth)) {
                eastOrWest = eastOrWest.getOpposite()
                northOrSouth = northOrSouth.getOpposite()
                overlapped = true
            }

            while ((!areCoordinatesInRange(tail.z, sourceLength, end.z, targetLength)
                            || areDiagonal(tail, sourceLength, end, targetLength)
                            || areOverlapping(tail, sourceLength, end, targetLength))
                    && (overlapped || !areOverlapping(tail.step(northOrSouth), sourceLength, end, targetLength))
                    && canTraverse(collision, tail, sourceWidth, sourceLength, northOrSouth, projectile)) {
                tail = tail.step(northOrSouth)
                path.add(tail)
            }

            while ((!areCoordinatesInRange(tail.x, sourceWidth, end.x, targetWidth)
                            || areDiagonal(tail, sourceWidth, end, targetWidth)
                            || areOverlapping(tail, sourceWidth, end, targetWidth))
                    && (overlapped || !areOverlapping(tail.step(eastOrWest), sourceWidth, end, targetWidth))
                    && canTraverse(collision, tail, sourceWidth, sourceLength, eastOrWest, projectile)) {
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

    private fun areBordering(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areBordering(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)

    private fun areDiagonal(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areDiagonal(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)

    private fun areOverlapping(tile1: Tile, size1: Int, tile2: Tile, size2: Int): Boolean = AabbUtil.areOverlapping(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)

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
}