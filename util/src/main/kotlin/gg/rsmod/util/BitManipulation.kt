package gg.rsmod.util

/**
 * @author Tom <rspsmods@gmail.com>
 */
object BitManipulation {

    /**
     * Extracts the value from [packed] from the bits found in [startBit] to [endBit].
     */
    fun getBit(packed: Int, startBit: Int, endBit: Int): Int {
        val position = DataConstants.BIT_SIZES[endBit - startBit]
        return ((packed shr startBit) and position)
    }

    /**
     * Calculates the new [packed] value after editing the value in between the
     * [startBit] and [endBit].
     */
    fun setBit(packed: Int, startBit: Int, endBit: Int, value: Int): Int {
        val maxValue = DataConstants.BIT_SIZES[endBit - startBit] shl startBit
        return (packed and maxValue.inv()) or ((value shl startBit) and maxValue)
    }
}