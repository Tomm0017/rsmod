package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.path.PathfindingStrategy
import org.apache.logging.log4j.LogManager
import java.util.*
import kotlin.collections.ArrayList

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

    override fun calculatePath(origin: Tile, target: Tile, type: EntityType, validSurroundingTiles: Array<Tile>?): Queue<Tile> {
        if (!target.isWithinRadius(origin, MAX_DISTANCE)) {
            logger.error("Target tile is not within view distance of origin. [origin=$origin, target=$target, distance=${origin.getDistance(target)}]")
            return ArrayDeque()
        }

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()
        val surroundingNodes = ArrayList<Node>(if (validSurroundingTiles != null) 8 else 0)

        nodes.add(Node(tile = origin, parent = null))

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
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && world.collision.canTraverse(head.tile, direction, type)) {
                    node.cost = head.cost + 1
                    nodes.add(node)
                    closed.add(node)
                    if (validSurroundingTiles != null && node.tile in validSurroundingTiles) {
                        surroundingNodes.add(node)
                    }
                }
            }
        }

        if (maxSearch == 0) {
            logger.warn("Had to exit path early as max search samples ran out. [origin=$origin, target=$target, distance=${origin.getDistance(target)}]")
        }

        if (tail == null && surroundingNodes.isNotEmpty()) {
            tail = surroundingNodes.minBy { it.cost }
        }

        if (tail == null && closed.isNotEmpty()) {
            val min = closed.minBy { it.tile.getDistance(target) }!!
            val valid = closed.filter { !it.tile.sameAs(origin) && it.tile.getDistance(target) <= min.tile.getDistance(target) }
            if (valid.isNotEmpty()) {
                tail = valid.minBy { it.tile.getDelta(origin) }
            }
        }

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null) {
            path.addFirst(tail.tile)
            tail = tail.parent
        }

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