package gg.rsmod.net.packet

/**
 * An enumeration which holds the mode a [GamePacketBuilder] or [GamePacketReader] can be in.
 *
 * @author Graham
 */
enum class AccessMode {

    /**
     * When in bit access mode, bits can be written and packed into bytes.
     */
    BIT_ACCESS,

    /**
     * When in byte access modes, bytes are written directly to the buffer.
     */
    BYTE_ACCESS

}