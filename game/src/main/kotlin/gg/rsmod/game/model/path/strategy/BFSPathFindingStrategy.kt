package gg.rsmod.game.model.path.strategy

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.util.*

/**
 * A [PathFindingStrategy] which uses Breadth-first Search Algorithm to calculate
 * a valid path to the target location.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class BFSPathFindingStrategy(collision: CollisionManager) : PathFindingStrategy(collision) {

    override fun calculateRoute(request: PathRequest): Route {
        val start = request.start
        val end = request.end

        val sourceWidth = request.sourceWidth
        val sourceLength = request.sourceLength
        val projectilePath = request.projectilePath
        val projectile = projectilePath || request.touchRadius > 1

        val clipNode = request.clipFlags.contains(PathRequest.ClipFlag.NODE)
        val clipLink = request.clipFlags.contains(PathRequest.ClipFlag.LINKED_NODE)

        val validEndTiles = getEndTiles(request)

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

            val inRange = head.tile in validEndTiles && (!projectile || collision.raycast(head.tile, end, projectilePath))
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
                if (!closed.contains(node) && start.isWithinRadius(tile, MAX_DISTANCE)
                        && !isStepBlocked(head.tile, tile, sourceWidth, sourceLength, clipNode, clipLink)) {
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

    private fun isTileBlocked(node: Tile, link: Tile): Boolean = !collision.canTraverse(node, Direction.between(node, link), projectile = false)

    private fun isStepBlocked(node: Tile, link: Tile, width: Int, length: Int, clipNode: Boolean, clipLink: Boolean): Boolean {
        if (!clipNode && !clipLink) {
            return false
        }

        for (x in 0 until width) {
            for (z in 0 until length) {
                val transform = node.transform(x, z)
                if (clipNode && isTileBlocked(transform, link)) {
                    return true
                }
                if (clipLink && isTileBlocked(link, transform)) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * Checks if the direction outwards of the target is blocked.
     */
    private fun isTargetDirectionBlocked(node: Tile, end: Tile, targetWidth: Int, targetLength: Int,
                                         blockedDirection: Set<Direction>): Boolean {
        val x = node.x
        val z = node.z
        val dx = x - end.x
        val dz = z - end.z

        val face = when {
            (dx == -1) -> Direction.EAST
            (dx == targetWidth) -> Direction.WEST
            (dz == -1) -> Direction.NORTH
            (dz == targetLength) -> Direction.SOUTH
            else -> return false
        }

        return blockedDirection.contains(face.getOpposite())
    }

    private fun isDirectionBlocked(node: Tile, end: Tile, targetWidth: Int, targetLength: Int,
                                   projectilePath: Boolean): Boolean {
        val x = node.x
        val z = node.z
        val dx = x - end.x
        val dz = z - end.z

        val face = when {
            (dx == -1) -> Direction.EAST
            (dx == targetWidth) -> Direction.WEST
            (dz == -1) -> Direction.NORTH
            (dz == targetLength) -> Direction.SOUTH
            else -> return false
        }

        return collision.isBlocked(node, face, projectile = projectilePath)
    }

    private fun isDiagonalTile(current: Tile, end: Tile, targetWidth: Int, targetLength: Int): Boolean {
        val curX = current.x
        val curZ = current.z
        val endX = end.x
        val endZ = end.z

        val southWest = curX == (endX - 1) && curZ == (endZ - 1)
        val southEast = curX == (endX + targetWidth) && curZ == (endZ - 1)
        val northWest = curX == (endX - 1) && curZ == (endZ + targetLength)
        val northEast = curX == (endX + targetWidth) && curZ == (endZ + targetLength)

        return southWest || southEast || northWest || northEast
    }

    private fun isTileOverlapping(tile: Tile, target: Tile, targetWidth: Int, targetLength: Int): Boolean {
        val curX = tile.x
        val curZ = tile.z
        val endX = target.x
        val endZ = target.z

        return (curX >= endX && curX < endX + targetWidth && curZ >= endZ && curZ < endZ + targetLength)
    }

    private fun getEndTiles(request: PathRequest): Set<Tile> {
        val end = request.end
        val targetWidth = request.targetWidth
        val targetLength = request.targetLength
        val touchRadius = request.touchRadius
        val projectilePath = request.projectilePath

        val clipDiagonals = request.clipFlags.contains(PathRequest.ClipFlag.DIAGONAL)
        val clipDirections = request.clipFlags.contains(PathRequest.ClipFlag.DIRECTIONS)
        val clipOverlapping = request.clipFlags.contains(PathRequest.ClipFlag.OVERLAP)

        val validTiles = hashSetOf<Tile>()

        if (targetWidth == 0 && targetLength == 0) {
            validTiles.add(end)
        } else {
            for (x in -1..targetWidth) {
                for (z in -1..targetLength) {
                    val tile = end.transform(x, z)

                    if (clipDiagonals && isDiagonalTile(tile, end, targetWidth, targetLength)
                            || clipDirections && isTargetDirectionBlocked(tile, end, targetWidth, targetLength, request.blockedDirections)
                            || clipOverlapping && isTileOverlapping(tile, end, targetWidth, targetLength)) {
                        continue
                    }

                    if (!isDirectionBlocked(tile, end, targetWidth, targetLength, projectilePath)) {
                        validTiles.add(tile)
                    }
                }
            }
        }

        if (touchRadius > 1) {
            for (x in -touchRadius..touchRadius) {
                for (z in -touchRadius..touchRadius) {
                    if (x in 0 until targetWidth && z in 0 until targetLength) {
                        continue
                    }
                    val tile = end.transform(x, z)
                    validTiles.add(tile)
                }
            }
        }

        return validTiles
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