package gg.rsmod.net.packet

import io.netty.buffer.ByteBuf

/**
 * Represents a single packet used in the in-game protocol.
 *
 * @author Graham
 */
class GamePacket
/**
 * Creates the game packet.
 *
 * @param opcode The opcode.
 * @param type The packet type.
 * @param payload The payload.
 */
(
        /**
         * The opcode.
         */
        /**
         * Gets the opcode.
         *
         * @return The opcode.
         */
        val opcode: Int,
        /**
         * The packet type.
         */
        /**
         * Gets the packet type.
         *
         * @return The packet type.
         */
        val type: PacketType,
        /**
         * The payload.
         */
        /**
         * Gets the payload.
         *
         * @return The payload.
         */
        val payload: ByteBuf) {

    /**
     * The length.
     */
    /**
     * Gets the payload length.
     *
     * @return The payload length.
     */
    val length: Int = payload.readableBytes()

}