package gg.rsmod.net.packet

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface IPacketMetadataHelper {

    fun getType(opcode: Int): PacketType?

    fun getLength(opcode: Int): Int
}