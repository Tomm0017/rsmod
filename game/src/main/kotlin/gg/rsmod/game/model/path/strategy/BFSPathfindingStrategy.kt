package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.PathfindingStrategy
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathfindingStrategy(override val collision: CollisionManager) : PathfindingStrategy(collision) {

    companion object {
        private val logger = LogManager.getLogger(BFSPathfindingStrategy::class.java)
    }

    override fun calculatePath(origin: Tile, target: Tile, type: EntityType, request: PathRequest,
                               targetWidth: Int, targetLength: Int): Queue<Tile> {
        if (!target.isWithinRadius(origin, MAX_DISTANCE)) {
            logger.error("Target tile is not within view distance of origin. [origin=$origin, target=$target, distance=${origin.calculateDistance(target)}]")
            return ArrayDeque()
        }

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()

        nodes.add(Node(tile = origin, parent = null))

        var tail: Node? = null

        var maxSearch = 2048
        while (nodes.isNotEmpty() && !request.discard && maxSearch-- > 0) {
            val head = nodes.poll()

            if (head.tile.sameAs(target)) {
                tail = head
                break
            }

            Direction.NWSE_NWNESWSE.forEach { direction ->
                val tile = head.tile.step(1, direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && collision.canTraverse(head.tile, direction, type)) {
                    nodes.add(node)
                    closed.add(node)
                }
            }
        }

        if (tail == null) {
            if (targetLength <= 1 && targetWidth <= 1) {
                /**
                 * We get the closest node to the [target], but keep in mind that
                 * multiple nodes may have the same distance (i.e the first layer
                 * of border outside of the [target]).
                 */
                val min = closed.minBy { it.tile.calculateDistance(target) }!!

                /**
                 * The destination will be the closest tile to the player, out of
                 * the nodes that have the minimum distance from the [target].
                 * This is so that the destination will be the closest to the
                 * [origin].
                 */
                tail = closed.filter { !it.tile.sameAs(origin) && it.tile.calculateDistance(target) <= min.tile.calculateDistance(target) }
                        .minBy { it.tile.calculateDelta(origin) }
            } else {
                /**
                 * TODO: this is different per object rot and shit. We need a separate
                 * method for pathing on objects so we can just pass the game obj
                 * as a parameter. all this complicated shit should be there instead of here.
                 * this one should be the default getPath for walking/simple tasks
                 */
                val neighbors = arrayListOf<Tile>()
                for (x in 0 until targetWidth) {
                    for (z in 0..targetLength) {
                        val tile = target.transform(x, z)
                        neighbors.add(tile)
                    }
                }

                var bestCost = Int.MAX_VALUE
                val valid = closed.filter { !it.tile.sameAs(origin) && neighbors.contains(it.tile) }
                for (node in valid) {
                    val cost = (node.tile.calculateDistance(origin) * 2) + node.tile.calculateDelta(target)
                    if (cost < bestCost) {
                        tail = node
                        bestCost = cost
                    }
                }
            }
        }

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null && !request.discard) {
            path.addFirst(tail.tile)
            tail = tail.parent
        }

        return path
    }

    private data class Node(val tile: Tile, var parent: Node?) {

        override fun equals(other: Any?): Boolean = (other as? Node)?.tile?.sameAs(tile) ?: false

        override fun hashCode(): Int = tile.hashCode()
    }
}