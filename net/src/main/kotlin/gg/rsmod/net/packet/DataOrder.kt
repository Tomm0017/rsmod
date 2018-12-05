package gg.rsmod.net.packet

/**
 * Represents the order of bytes in a [DataType] when [DataType.getBytes] `> 1`.
 *
 * @author Graham
 */
enum class DataOrder {

    /**
     * Most significant byte to least significant byte.
     */
    BIG,

    /**
     * Also known as the V2 order.
     */
    INVERSED_MIDDLE,

    /**
     * Least significant byte to most significant byte.
     */
    LITTLE,

    /**
     * Also known as the V1 order.
     */
    MIDDLE

}