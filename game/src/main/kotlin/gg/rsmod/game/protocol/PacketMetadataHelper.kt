package gg.rsmod.game.protocol

import gg.rsmod.game.message.MessageStructureSet
import gg.rsmod.net.packet.IPacketMetadataHelper
import gg.rsmod.net.packet.PacketType
import org.apache.logging.log4j.LogManager

/**
 * A [IPacketMetadataHelper] implementation that is responsible for exposing
 * packet metadata based on their opcode.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PacketMetadataHelper(private val structures: MessageStructureSet) : IPacketMetadataHelper {

    companion object {
        private val logger = LogManager.getLogger(PacketMetadataHelper::class.java)
    }

    override fun getType(opcode: Int): PacketType {
        val structure = structures.get(opcode)
        if (structure == null) {
            logger.warn("No message structure found for message with opcode {}.", opcode)
            return PacketType.FIXED
        }
        return structure.type
    }

    override fun getLength(opcode: Int): Int {
        val structure = structures.get(opcode)
        if (structure == null) {
            logger.warn("No message structure found for message with opcode {}.", opcode)
            return 0
        }
        return structure.length
    }
}
