package gg.rsmod.util

/**
 * Utility methods for axis-aligned bounding boxes.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object AabbUtil {

    data class Box(val x: Int, val z: Int, val width: Int, val length: Int) {

        val x1: Int get() = x

        val x2: Int get() = x + width

        val z1: Int get() = z

        val z2: Int get() = z + length
    }

    /**
     * Checks to see if two AABB are bordering, but not overlapping.
     */
    fun areBordering(x1: Int, z1: Int, width1: Int, length1: Int,
                     x2: Int, z2: Int, width2: Int, length2: Int): Boolean {
        val a = Box(x1, z1, width1 - 1, length1 - 1)
        val b = Box(x2, z2, width2 - 1, length2 - 1)

        if (b.x1 in a.x1 .. a.x2 && b.z1 in a.z1 .. a.z2 || b.x2 in a.x1 .. a.x2 && b.z2 in a.z1 .. a.z2) {
            return false
        }

        if (b.x1 > a.x2 + 1) {
            return false
        }

        if (b.x2 < a.x1 - 1) {
            return false
        }

        if (b.z1 > a.z2 + 1) {
            return false
        }

        if (b.z2 < a.z1 - 1) {
            return false
        }
        return true
    }

    fun areDiagonal(x1: Int, z1: Int, width1: Int, length1: Int,
                    x2: Int, z2: Int, width2: Int, length2: Int): Boolean {
        val a = Box(x1, z1, width1 - 1, length1 - 1)
        val b = Box(x2, z2, width2 - 1, length2 - 1)

        /**
         * South-west diagonal tile.
         */
        if (a.x1 - 1 == b.x2 && a.z1 - 1 == b.z2) {
            return true
        }

        /**
         * South-east diagonal tile.
         */
        if (a.x2 + 1 == b.x2 && a.z1 - 1 == b.z2) {
            return true
        }

        /**
         * North-west diagonal tile.
         */
        if (a.x1 - 1 == b.x2 && a.z2 + 1 == b.z2) {
            return true
        }

        /**
         * North-east diagonal tile.
         */
        if (a.x2 + 1 == b.x2 && a.z2 + 1 == b.z2) {
            return true
        }

        return false
    }

    fun areOverlapping(x1: Int, z1: Int, width1: Int, length1: Int,
                       x2: Int, z2: Int, width2: Int, length2: Int): Boolean {
        val a = Box(x1, z1, width1 - 1, length1 - 1)
        val b = Box(x2, z2, width2 - 1, length2 - 1)

        if (a.x1 > b.x2 || b.x1 > a.x2) {
            return false
        }

        if (a.z1 > b.z2 || b.z1 > a.z2) {
            return false
        }

        return true
    }
}