package gg.rsmod.game.model.path

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.path.PathRequest.ClipFlag
import java.util.*

/**
 * Represents attributes and parameters that a [PathFindingStrategy] can use when
 * calculating its route.
 *
 * @param start
 * The start tile, also known as Point A.
 *
 * @param sourceWidth
 * The width size of the node requesting the path.
 *
 * @param sourceLength
 * The length size of the node requesting the path.
 *
 * @param end
 * The destination tile, also known as Point B.
 *
 * @param targetWidth
 * The width size of the destination node.
 *
 * @param targetLength
 * The length size of the destination node.
 *
 * @param touchRadius
 * The radius from [end], in tiles, in which the path can complete.
 *
 * @param projectilePath
 * If the path should take projectile collision data into account instead of
 * normal pawn collision data.
 *
 * @param clipFlags
 * A [Set] of [ClipFlag]s that are used to block, or "clip", certain tiles while
 * creating a route.
 *
 * @param blockedDirections
 * A [Set] of blocked directions that are bordering the end tile, taking into
 * account the [targetWidth] and [targetLength].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PathRequest private constructor(val start: Tile, val sourceWidth: Int, val sourceLength: Int, val end: Tile,
                                      val targetWidth: Int, val targetLength: Int, val touchRadius: Int, val projectilePath: Boolean,
                                      val clipFlags: EnumSet<ClipFlag>, val blockedDirections: EnumSet<Direction>) {

    enum class ClipFlag {
        /**
         * Clip diagonal tiles.
         */
        DIAGONAL,

        /**
         * Clip certain directions. The directions are specified elsewhere.
         * This is used for things such as walking to objects that have directions
         * which you can't interact from.
         */
        DIRECTIONS,

        /**
         * Clip overlapping tiles in respect to the target. For example for an
         * npc that has a size of 3x3, it will clip the tiles within the 3x3
         * area that the npc occupies.
         */
        OVERLAP,

        /**
         * Clip the nodes.
         * Nodes are the 'current' tile the path-finder is iterating over to
         * determine if it's a "valid" tile.
         */
        NODE,

        /**
         * Clip the linked nodes.
         * Linked nodes are the tiles in front of the [NODE] tile in a specific
         * direction.
         */
        LINKED_NODE,
    }

    class Builder {

        private var start: Tile? = null

        private var end: Tile? = null

        private var sourceWidth = -1

        private var sourceLength = -1

        private var targetWidth = -1

        private var targetLength = -1

        private var touchRadius = -1

        private var projectilePath = false

        private val clipFlags = EnumSet.noneOf(ClipFlag::class.java)

        private val blockedDirections = EnumSet.noneOf(Direction::class.java)

        fun build(): PathRequest {
            check(start != null && end != null) { "Points must be set." }
            check(sourceWidth != -1 && sourceLength != -1) { "Source size must be set." }
            check(targetWidth != -1 && targetLength != -1) { "Target size must be set." }

            if (touchRadius == -1) {
                touchRadius = 0
            }

            return PathRequest(start!!, sourceWidth, sourceLength, end!!, targetWidth, targetLength, touchRadius, projectilePath,
                    clipFlags, blockedDirections)
        }

        /**
         * @see PathRequest.start
         * @see PathRequest.end
         */
        fun setPoints(start: Tile, end: Tile): Builder {
            check(this.start == null && this.end == null) { "Points have already been set." }
            this.start = start
            this.end = end
            return this
        }

        /**
         * @see PathRequest.sourceWidth
         * @see PathRequest.sourceLength
         */
        fun setSourceSize(width: Int, length: Int): Builder {
            check(this.sourceWidth == -1 && this.sourceLength == -1) { "Source size has already been set." }
            this.sourceWidth = width
            this.sourceLength = length
            return this
        }

        /**
         * @see PathRequest.targetWidth
         * @see PathRequest.targetLength
         */
        fun setTargetSize(width: Int, length: Int): Builder {
            check(this.targetWidth == -1 && this.targetLength == -1) { "Target size has already been set." }
            this.targetWidth = width
            this.targetLength = length
            return this
        }

        /**
         * @see PathRequest.touchRadius
         */
        fun setTouchRadius(touchRadius: Int): Builder {
            check(this.touchRadius == -1) { "Touch radius has already been set." }
            this.touchRadius = touchRadius
            return this
        }

        /**
         * @see PathRequest.projectilePath
         */
        fun findProjectilePath(): Builder {
            check(!projectilePath) { "Projectile path flag has already been set." }
            this.projectilePath = true
            return this
        }

        /**
         * @see PathRequest.projectilePath
         */
        fun setProjectilePath(projectile: Boolean): Builder {
            this.projectilePath = projectile
            return this
        }

        /**
         * @see PathRequest.clipFlags
         * @see ClipFlag.DIAGONAL
         */
        fun clipDiagonalTiles(): Builder {
            check(!clipFlags.contains(ClipFlag.DIAGONAL)) { "Diagonal tiles have already been flagged for clipping." }
            clipFlags.add(ClipFlag.DIAGONAL)
            return this
        }

        /**
         * @see PathRequest.blockedDirections
         * @see ClipFlag.DIRECTIONS
         */
        fun clipDirections(vararg blockedDirection: Direction): Builder {
            check(!clipFlags.contains(ClipFlag.DIRECTIONS)) { "A set of directions have already been flagged for clipping." }
            clipFlags.add(ClipFlag.DIRECTIONS)
            blockedDirections.addAll(blockedDirection)
            return this
        }

        /**
         * @see PathRequest.clipFlags
         * @see ClipFlag.OVERLAP
         */
        fun clipOverlapTiles(): Builder {
            check(!clipFlags.contains(ClipFlag.OVERLAP)) { "Overlapped tiles have already been flagged for clipping." }
            clipFlags.add(ClipFlag.OVERLAP)
            return this
        }

        /**
         * @see PathRequest.clipFlags
         * @see ClipFlag.NODE
         * @see ClipFlag.LINKED_NODE
         */
        fun clipPathNodes(node: Boolean, link: Boolean): Builder {
            check(!clipFlags.contains(ClipFlag.NODE) && !clipFlags.contains(ClipFlag.LINKED_NODE)) { "Path nodes have already been flagged for clipping." }
            if (node) {
                clipFlags.add(ClipFlag.NODE)
            }
            if (link) {
                clipFlags.add(ClipFlag.LINKED_NODE)
            }
            return this
        }
    }

    companion object {

        /**
         * Creates a default walk request.
         */
        fun createWalkRequest(pawn: Pawn, x: Int, z: Int, projectile: Boolean): PathRequest = Builder()
                .setPoints(start = Tile(pawn.tile), end = Tile(x, z, pawn.tile.height))
                .setSourceSize(width = pawn.getSize(), length = pawn.getSize())
                .setTargetSize(width = 0, length = 0)
                .setProjectilePath(projectile)
                .clipPathNodes(node = true, link = true)
                .build()
    }
}