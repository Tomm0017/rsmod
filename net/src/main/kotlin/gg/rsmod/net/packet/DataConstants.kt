package gg.rsmod.net.packet

/**
 * A class holding data-related constants.
 *
 * @author Graham
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
}