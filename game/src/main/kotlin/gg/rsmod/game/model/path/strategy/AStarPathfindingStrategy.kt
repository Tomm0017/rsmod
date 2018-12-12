package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.entity.EntityType
import gg.rsmod.game.model.path.PathfindingStrategy
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AStarPathfindingStrategy(override val collision: CollisionManager)
    : PathfindingStrategy(collision) {

    override fun getPath(origin: Tile, target: Tile, type: EntityType): Deque<Tile> {
        val nodes = hashMapOf<Tile, Node>()
        val start = Node(origin)
        val end = Node(target)

        nodes[origin] = start
        nodes[target] = end

        val open = hashSetOf<Node>()
        val sorted = PriorityQueue<Node>()
        open.add(start)
        sorted.add(start)

        var max = 4096
        do {
            val active = getCheapest(sorted)
            val position = active.tile

            if (position.sameAs(target)) {
                break
            }

            open.remove(active)
            active.open = false

            val x = position.x
            val z = position.z

            for (nextX in x - 1..x + 1) {
                for (nextZ in z - 1..z + 1) {
                    if (nextX == x && nextZ == z) {
                        continue
                    }
                    val adjacent = Tile(nextX, nextZ, position.height)
                    val direction = Direction.between(adjacent, position)

                    if (collision.canTraverse(adjacent, type, direction)) {
                        val node = nodes.computeIfAbsent(adjacent) { Node(adjacent) }
                        compare(active, node, open, sorted)
                    }
                }
            }
        } while (open.isNotEmpty() && max-- > 0)

        val shortest = ArrayDeque<Tile>()

        var active = end
        if (active.parent != null) {
            var position = active.tile
            while (!origin.sameAs(position)) {
                shortest.addFirst(position)
                active = active.parent!!
                position = active.tile
            }
        }
        return shortest
    }

    private fun compare(active: Node, other: Node, open: MutableSet<Node>, sorted: Queue<Node>) {
        val cost = active.cost + active.tile.calculateDistance(other.tile)

        if (other.cost > cost) {
            open.remove(other)
            other.open = false
        } else if (other.open && !open.contains(other)) {
            other.cost = cost
            other.parent = active
            open.add(other)
            sorted.add(other)
        }
    }

    private fun getCheapest(nodes: Queue<Node>): Node {
        var node = nodes.peek()
        while (!node.open) {
            nodes.poll()
            node = nodes.peek()
        }
        return node
    }

    private data class Node(val tile: Tile) : Comparable<Node> {

        var cost: Int = 0
        var parent: Node? = null
        var open = true

        override fun compareTo(other: Node): Int = Integer.compare(cost, other.cost)

        override fun equals(other: Any?): Boolean {
            if (other is Node) {
                return tile.sameAs(other.tile)
            }
            return false
        }

        override fun hashCode(): Int = tile.hashCode()
    }
}