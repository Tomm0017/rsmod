package gg.rsmod.net.codec.game

import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.io.IsaacRandom
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import mu.KotlinLogging
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GamePacketEncoder(private val random: IsaacRandom, private val rsaEncryption: Boolean) : MessageToByteEncoder<GamePacket>() {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    override fun encode(ctx: ChannelHandlerContext, msg: GamePacket, out: ByteBuf) {
        if (msg.type == PacketType.VARIABLE_BYTE && msg.length >= 256) {
            logger.error("Message length {} too long for 'variable-byte' packet on channel {}.", DecimalFormat().format(msg.length), ctx.channel())
            return
        } else if (msg.type == PacketType.VARIABLE_SHORT && msg.length >= 65536) {
            logger.error("Message length {} too long for 'variable-short' packet on channel {}.", DecimalFormat().format(msg.length), ctx.channel())
            return
        }
        out.writeByte(msg.opcode + (if (rsaEncryption) (random.nextInt() and 0xFF) else 0))
        when (msg.type) {
            PacketType.VARIABLE_BYTE -> out.writeByte(msg.length)
            PacketType.VARIABLE_SHORT -> out.writeShort(msg.length)
            else -> {}
        }
        out.writeBytes(msg.payload)
        msg.payload.release()
    }

}