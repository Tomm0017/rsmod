package gg.rsmod.util

/**
 * A class holding data-related constants.
 *
 * @author Graham
 * @author Tom <rspsmods@gmail.com>
 */
object DataConstants {

    /**
     * An array of bit masks. The element `n` is equal to `2<sup>n</sup> - 1`.
     */
    val BIT_MASK = IntArray(32).apply {
        for (i in 0 until size) {
            set(i, (1 shl i) - 1)
        }
    }

    val BIT_SIZES = IntArray(32).apply {
        var size = 2
        for (i in 0 until this.size) {
            set(i, size - 1)
            size += size
        }
    }
}