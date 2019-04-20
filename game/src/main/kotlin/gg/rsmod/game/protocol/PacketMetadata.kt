package gg.rsmod.game.protocol

import gg.rsmod.game.message.MessageStructureSet
import gg.rsmod.net.packet.IPacketMetadata
import gg.rsmod.net.packet.PacketType
import mu.KLogging

/**
 * An [IPacketMetadata] implementation that is responsible for exposing
 * packet metadata based on their opcode.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PacketMetadata(private val structures: MessageStructureSet) : IPacketMetadata {

    /**
     * Logging in case of null value should be handled in usage implementation.
     */
    override fun getType(opcode: Int): PacketType? = structures.get(opcode)?.type

    override fun getLength(opcode: Int): Int {
        val structure = structures.get(opcode)
        if (structure == null) {
            logger.warn("No message structure found for message with opcode {}.", opcode)
            return 0
        }
        return structure.length
    }

    override fun shouldIgnore(opcode: Int): Boolean {
        val structure = structures.get(opcode)
        if (structure == null) {
            logger.warn("No message structure found for message with opcode {}.", opcode)
            return true
        }
        return structure.ignore
    }

    companion object : KLogging()
}
