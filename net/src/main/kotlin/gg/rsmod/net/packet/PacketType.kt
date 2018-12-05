package gg.rsmod.net.packet

/**
 * An enumeration which contains the different types of packets.
 *
 * @author Graham
 */
enum class PacketType {

    /**
     * A packet where the length is known by both the client and server already.
     */
    FIXED,

    /**
     * A packet with no header.
     */
    RAW,

    /**
     * A packet where the length is sent to its destination with it as a byte.
     */
    VARIABLE_BYTE,

    /**
     * A packet where the length is sent to its destination with it as a short.
     */
    VARIABLE_SHORT,

    /**
     * An incoming packet that should be ignored.
     */
    IGNORE,

}