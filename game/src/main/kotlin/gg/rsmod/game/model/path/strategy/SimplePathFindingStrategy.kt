package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
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
        val sourceWidth = request.sourceWidth
        val targetWidth = request.targetWidth
        val targetTiles = getTargetTiles(request)

        val path = ArrayDeque<Tile>()
        path.add(start)



        val tail = path.peekLast()
        return Route(path, success = areBordering(tail, sourceWidth - 1, end, targetWidth - 1), tail = tail)
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

        for (x in -touchRadius .. touchRadius) {
            for (z in -touchRadius .. touchRadius) {
                if (x in 0 until targetWidth && z in 0 until targetLength) {
                    continue
                }
                val tile = end.transform(x, z)
                targetTiles.add(tile)
            }
        }

        return targetTiles
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