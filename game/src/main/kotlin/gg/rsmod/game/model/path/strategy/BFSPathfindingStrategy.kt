package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathfindingStrategy
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * A [PathfindingStrategy] which uses breadth-first search algorithm to calculate
 * a valid path to the target location.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathfindingStrategy(override val world: World) : PathfindingStrategy(world) {

    companion object {
        private val logger = LogManager.getLogger(BFSPathfindingStrategy::class.java)
    }

    override fun calculatePath(start: Tile, target: Tile, type: EntityType): Queue<Tile> {
        if (!target.isWithinRadius(start, MAX_DISTANCE)) {
            logger.error("Target tile is not within view distance of start. [start=$start, target=$target, distance=${start.getDistance(target)}]")
            return ArrayDeque()
        }

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()

        nodes.add(Node(tile = start, parent = null))

        var tail: Node? = null

        var maxSearch = (256 * 10)
        while (nodes.isNotEmpty() && maxSearch-- > 0) {
            val head = nodes.poll()

            if (head.tile.sameAs(target)) {
                tail = head
                break
            }

            Direction.RS_ORDER.forEach { direction ->
                val tile = head.tile.step(direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && (world.collision.canTraverse(head.tile, direction, type) && world.collision.canTraverse(tile, direction.getOpposite(), type))) {
                    node.cost = head.cost + 1
                    nodes.add(node)
                    closed.add(node)
                }
            }
        }

        if (maxSearch == 0) {
            logger.warn("Had to exit path early as max search samples ran out. [origin=$start, target=$target, distance=${start.getDistance(target)}]")
        }

        if (tail == null && closed.isNotEmpty()) {
            val min = closed.minBy { it.tile.getDistance(target) }!!
            val valid = closed.filter { it.tile.getDistance(target) <= min.tile.getDistance(target) }
            if (valid.isNotEmpty()) {
                tail = valid.minBy { it.tile.getDelta(start) }
            }
        }

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null) {
            path.addFirst(tail.tile)
            tail = tail.parent
        }
        path.addFirst(start)

        return path
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