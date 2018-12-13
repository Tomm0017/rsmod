package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.path.PathfindingStrategy
import gg.rsmod.game.model.path.plugins.ObjectPathing
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathfindingStrategy(override val world: World) : PathfindingStrategy(world) {
    companion object {

        private val logger = LogManager.getLogger(BFSPathfindingStrategy::class.java)
    }
    override fun calculatePath(origin: Tile, target: Tile, type: EntityType): Queue<Tile> {
        if (!target.isWithinRadius(origin, MAX_DISTANCE)) {
            logger.error("Target tile is not within view distance of origin. [origin=$origin, target=$target, distance=${origin.calculateDistance(target)}]")
            return ArrayDeque()
        }

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()

        nodes.add(Node(tile = origin, parent = null))

        var tail: Node? = null

        var maxSearch = 2048
        while (nodes.isNotEmpty() && maxSearch-- > 0) {
            val head = nodes.poll()

            if (head.tile.sameAs(target)) {
                tail = head
                break
            }

            Direction.RS_ORDER.forEach { direction ->
                val tile = head.tile.step(1, direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && world.collision.canTraverse(head.tile, direction, type)) {
                    nodes.add(node)
                    closed.add(node)
                }
            }
        }

        if (tail == null) {
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
        }

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null) {
            path.addFirst(tail.tile)
            tail = tail.parent
        }

        return path
    }

    override fun calculatePath(origin: Tile, obj: GameObject, type: EntityType): Queue<Tile> {
        val target = obj.tile

        if (!target.isWithinRadius(origin, MAX_DISTANCE)) {
            logger.error("Target tile is not within view distance of origin. [origin=$origin, target=$target, distance=${origin.calculateDistance(target)}]")
            return ArrayDeque()
        }

        val validTiles = ObjectPathing.getValidTiles(world.definitions, obj)

        val nodes = ArrayDeque<Node>()
        val closed = hashSetOf<Node>()
        val validNodes = arrayListOf<Node>()

        nodes.add(Node(tile = origin, parent = null))

        var tail: Node? = null

        var maxSearch = 2048
        while (nodes.isNotEmpty() && maxSearch-- > 0) {
            val head = nodes.poll()

            if (head.tile.sameAs(target)) {
                tail = head
                break
            }

            Direction.RS_ORDER.forEach { direction ->
                val tile = head.tile.step(1, direction)
                val node = Node(tile = tile, parent = head)
                if (!closed.contains(node) && head.tile.isWithinRadius(tile, MAX_DISTANCE) && world.collision.canTraverse(head.tile, direction, type)) {
                    nodes.add(node)
                    closed.add(node)
                    if (node.tile in validTiles) {
                        validNodes.add(node)
                    }
                }
            }
        }

        if (tail == null && validNodes.isNotEmpty()) {
            tail = validNodes.first()
        }

        if (tail == null) {
            val min = closed.minBy { it.tile.calculateDistance(target) }!!
            tail = closed.filter { !it.tile.sameAs(origin) && it.tile.calculateDistance(target) <= min.tile.calculateDistance(target) }
                    .minBy { it.tile.calculateDelta(origin) }
        }

        val path = ArrayDeque<Tile>()
        while (tail?.parent != null) {
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