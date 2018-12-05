package gg.rsmod.game.packet

import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.GamePacketReader
import gg.rsmod.net.packet.PacketType
import org.junit.Test

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BitTest {

    @Test
    fun reading() {
        val writer = GamePacketBuilder()
        writer.switchToBitAccess()
        writer.putBits(1, 1)
        writer.putBits(1, 1)
        writer.putBits(1, 0)
        writer.putBits(1, 1)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 1)
        writer.putBits(1, 1)
        writer.putBits(1, 1)
        writer.putBits(1, 0)
        writer.putBits(1, 0)
        writer.putBits(1, 1)
        writer.switchToByteAccess()

        val reader = GamePacketReader(GamePacket(opcode = 79, type = PacketType.VARIABLE_SHORT, payload = writer.getBuffer()))
        reader.switchToBitAccess()
        for (i in 0 until 20) {
            val value = reader.getBits(1)
            println("Reading bit $i: $value")
        }
        reader.switchToByteAccess()
    }
}