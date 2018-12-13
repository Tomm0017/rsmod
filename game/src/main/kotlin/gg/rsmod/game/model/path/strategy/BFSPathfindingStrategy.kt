package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.PathfindingStrategy
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathfindingStrategy(override val collision: CollisionManager) : PathfindingStrategy(collision) {

    override fun calculatePath(origin: Tile, target: Tile, type: EntityType, request: PathRequest): Queue<Tile> {
        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Tile>()

        nodes.add(Node(tile = origin, parent = null))

        var tail: Node? = null

        while (nodes.isNotEmpty() && !request.discard) {
            val head = nodes.poll()

            if (head.tile.sameAs(target)) {
                tail = head
                break
            }

            Direction.NWSE_NWNESWSE.forEach { direction ->
                val tile = head.tile.step(1, direction)
                if (!closed.contains(tile) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && collision.canTraverse(head.tile, direction, type)) {
                    val node = Node(tile = tile, parent = head)
                    nodes.add(node)
                    closed.add(tile)
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