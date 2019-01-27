package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import java.util.*

/**
 * A [PathFindingStrategy] which uses breadth-first search algorithm to calculate
 * a valid path to the target location.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathFindingStrategy(override val world: World) : PathFindingStrategy(world) {

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end
        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val projectilePath = request.projectilePath
        val targetTiles = getTargetTiles(request)

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()
        var tail: Node? = null
        var searchLimit = 256 * 10
        var success = false

        nodes.add(Node(tile = start, parent = null))

        mainLoop@
        while (nodes.isNotEmpty()) {
            if (searchLimit-- == 0) {
                break
            }
            val head = nodes.poll()
            if (head.tile in targetTiles && (!projectilePath || world.collision.raycast(head.tile, end, projectilePath))) {
                tail = head
                success = true
                break
            }
            val order = Direction.RS_ORDER.sortedBy { head.tile.step(it).getDelta(end) + head.tile.step(it).getDelta(head.tile) }
            for (direction in order) {
                val tile = head.tile.step(direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE)) {
                    var canTraverse = true
                    out@ for (x in 0 until sourceWidth) {
                        for (z in 0 until sourceLength) {
                            if (!request.validWalk.invoke(head.tile.transform(x, z), tile)) {
                                canTraverse = false
                                break@out
                            }
                        }
                    }
                    if (canTraverse) {
                        node.cost = head.cost + 1
                        nodes.add(node)
                        closed.add(node)
                    }
                }
            }
        }

        if (tail == null && closed.isNotEmpty()) {
            val min = closed.minBy { it.tile.getDistance(end) }!!
            val valid = closed.filter { it.tile.getDistance(end) <= min.tile.getDistance(end) }
            if (valid.isNotEmpty()) {
                tail = valid.minBy { it.tile.getDelta(start) }
            }
        }

        val last = tail?.tile

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null) {
            path.addFirst(tail.tile)
            tail = tail.parent
        }

        return Route(path = path, success = success, tail = last ?: start)
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

    /**
     * A [Node] represents an single tile in a path, which can have the previous
     * tile in the path attached to it as a parent node.
     */
    private data class Node(val tile: Tile, var parent: Node?) {

        /**
         * The amount of interconnected nodes from this node to its parents,
         * and their parents, and their parents ...
         */
        var cost: Int = 0

        override fun equals(other: Any?): Boolean = (other as? Node)?.tile?.sameAs(tile) ?: false

        override fun hashCode(): Int = tile.hashCode()
    }
}