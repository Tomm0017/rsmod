package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.util.*

/**
 * A [PathFindingStrategy] which uses breadth-first search algorithm to calculate
 * a valid path to the target location.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathFindingStrategy(world: World) : PathFindingStrategy(world) {

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end

        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val targetWidth = request.targetWidth
        val targetLength = request.targetLength
        val touchRadius = request.touchRadius
        val projectilePath = request.projectilePath

        val targetTiles = ObjectOpenHashSet<Tile>()

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

        val nodes = ArrayDeque<Node>()
        val closed = ObjectOpenHashSet<Node>()
        var tail: Node? = null
        var searchLimit = 256 * 10
        var success = false

        nodes.add(Node(tile = start, parent = null))

        val order = Arrays.copyOf(Direction.RS_ORDER, Direction.RS_ORDER.size)

        while (nodes.isNotEmpty()) {
            if (cancel) {
                return Route(path = ArrayDeque(0), success = false, tail = start)
            }
            if (searchLimit-- == 0) {
                break
            }
            val head = nodes.poll()

            val inRange = head.tile in targetTiles && (!projectilePath || world.collision.raycast(head.tile, end, projectilePath))
            if (inRange) {
                tail = head
                success = true
                break
            }

            order.sortBy {
                val step = head.tile.step(it)
                step.getDelta(end) + step.getDelta(head.tile)
            }

            for (direction in order) {
                val tile = head.tile.step(direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && canTraverse(request, head.tile, tile, sourceWidth, sourceLength)) {
                    node.cost = head.cost + 1
                    nodes.add(node)
                    closed.add(node)
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

    private fun canTraverse(request: PathRequest, node: Tile, to: Tile, width: Int, length: Int): Boolean {
        for (x in 0 until width) {
            for (z in 0 until length) {
                if (!request.validWalk.invoke(node.transform(x, z), to)) {
                    return false
                }
            }
        }
        return true
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