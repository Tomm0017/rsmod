package gg.rsmod.game.model.path

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PathRequest private constructor(val start: Tile, val sourceWidth: Int, val sourceLength: Int,
                                      val end: Tile, val targetWidth: Int, val targetLength: Int,
                                      private val borderValidation: List<(Tile) -> (Boolean)> = arrayListOf(),
                                      val validWalk: (Tile, Tile) -> (Boolean)) {

    val validateBorder: (Tile) -> (Boolean)
        get() = { node -> borderValidation.none { !it.invoke(node) } }

    class Builder {

        private var start: Tile? = null

        private var end: Tile? = null

        private var sourceWidth = -1

        private var sourceLength = -1

        private var targetWidth = -1

        private var targetLength = -1

        private var projectilePath = false

        private var borderValidations = arrayListOf<(Tile) -> (Boolean)>()

        private var walkValidation: ((Tile, Tile) -> (Boolean))? = null

        fun build(): PathRequest {
            check(start != null && end != null) { "Points must be set." }
            check(sourceWidth != -1 && sourceLength != -1) { "Source size must be set." }
            check(targetWidth != -1 && targetLength != -1) { "Target size must be set." }

            return PathRequest(start!!, sourceWidth, sourceLength, end!!, targetWidth, targetLength, borderValidations.toList(), walkValidation ?: { _, _ -> true })
        }

        fun setPoints(start: Tile, end: Tile): Builder {
            check(this.start == null && this.end == null) { "Points have already been set." }
            this.start = start
            this.end = end
            return this
        }

        fun setSourceSize(width: Int, length: Int): Builder {
            check(this.sourceWidth == -1 && this.sourceLength == -1) { "Source size has already been set." }
            this.sourceWidth = width
            this.sourceLength = length
            return this
        }

        fun setTargetSize(width: Int, length: Int): Builder {
            check(this.targetWidth == -1 && this.targetLength == -1) { "Target size has already been set." }
            this.targetWidth = width
            this.targetLength = length
            return this
        }

        fun findProjectilePath(): Builder {
            check(!projectilePath) { "Projectile path flag has already been set." }
            this.projectilePath = true
            return this
        }

        fun setProjectilePath(projectile: Boolean): Builder {
            this.projectilePath = projectile
            return this
        }

        fun clipDiagonalTiles(): Builder {
            check(start != null && end != null) { "Points must be set before tile validations." }
            check(sourceWidth != -1 && sourceLength != -1) { "Source size must be set before tile validations." }
            check(targetWidth != -1 && targetLength != -1) { "Target size must be set before tile validations." }
            check(targetWidth > 0 || targetLength > 0) { "Target size must be > 0 to disable diagonal tiles." }

            borderValidations.add { tile ->

                val x = tile.x
                val z = tile.z

                val width = targetWidth
                val length = targetLength
                val end = this.end!!

                val southWest = x == (end.x - 1) && z == (end.z - 1)
                val southEast = x == (end.x + width) && z == (end.z - 1)
                val northWest = x == (end.x - 1) && z == (end.z + length)
                val northEast = x == (end.x + width) && z == (end.z + length)
                !southWest && !southEast && !northWest && !northEast
            }

            return this
        }

        fun clipBorderTiles(collision: CollisionManager, vararg blockedDirection: Direction): Builder {
            check(start != null && end != null) { "Points must be set before tile validations." }
            check(sourceWidth != -1 && sourceLength != -1) { "Source size must be set before tile validations." }
            check(targetWidth != -1 && targetLength != -1) { "Target size must be set before tile validations." }
            check(targetWidth > 0 || targetLength > 0) { "Target size must be > 0 to disable diagonal tiles." }

            borderValidations.add { tile ->
                val x = tile.x
                val z = tile.z

                val end = this.end!!
                val width = targetWidth
                val length = targetLength

                val dx = x - end.x
                val dz = z - end.z

                val face = when {
                    (dx == -1) -> Direction.EAST
                    (dx == width) -> Direction.WEST
                    (dz == -1) -> Direction.NORTH
                    (dz == length) -> Direction.SOUTH
                    else -> Direction.NONE
                }
                face == Direction.NONE || !blockedDirection.contains(face.getOpposite()) && !collision.isBlocked(tile, face, projectile = projectilePath)
            }

            return this
        }

        fun clipOverlapTiles(): Builder {
            check(start != null && end != null) { "Points must be set before tile validations." }
            check(sourceWidth != -1 && sourceLength != -1) { "Source size must be set before tile validations." }
            check(targetWidth != -1 && targetLength != -1) { "Target size must be set before tile validations." }

            check(targetWidth != 0 && targetLength != 0) { "Target size must be > 0 to disable overlapping tiles." }

            borderValidations.add { tile ->
                val x = tile.x
                val z = tile.z

                val width = targetWidth
                val length = targetLength
                val end = this.end!!

                val overlap = (x >= end.x && x < end.x + width && z >= end.z && z < end.z + length)
                !overlap
            }

            return this
        }

        fun clipPathNodes(collision: CollisionManager, tile: Boolean, face: Boolean): Builder {
            check(walkValidation == null) { "Node validation has already been set." }

            walkValidation = { node, facing ->
                val nodeWalkable = !tile || collision.canTraverse(node, Direction.between(node, facing), projectilePath)
                val faceWalkable = !face || collision.canTraverse(facing, Direction.between(facing, node), projectilePath)
                nodeWalkable && faceWalkable
            }

            return this
        }
    }
}