package gg.rsmod.net.packet

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface IPacketMetadata {

    fun getType(opcode: Int): PacketType?

    fun getLength(opcode: Int): Int
}