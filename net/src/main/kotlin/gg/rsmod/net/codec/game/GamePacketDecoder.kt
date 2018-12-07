package gg.rsmod.net.codec.game

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.IPacketMetadataHelper
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.IsaacRandom
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GamePacketDecoder(private val random: IsaacRandom, private val rsaEncryption: Boolean,
                        private val packetMetadata: IPacketMetadataHelper) : StatefulFrameDecoder<GameDecoderState>(GameDecoderState.OPCODE) {

    companion object {
        private val logger = LogManager.getLogger(GamePacketDecoder::class.java)
    }

    private var opcode = 0

    private var length = 0

    private var type = PacketType.FIXED

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>, state: GameDecoderState) {
        when (state) {
            GameDecoderState.OPCODE -> decodeOpcode(ctx, buf)
            GameDecoderState.LENGTH -> decodeLength(buf)
            GameDecoderState.PAYLOAD -> decodePayload(buf, out)
        }
    }

    private fun decodeOpcode(ctx: ChannelHandlerContext, buf: ByteBuf) {
        if (buf.isReadable) {
            opcode = (buf.readUnsignedByte().toInt() - (if (rsaEncryption) (random.nextInt() and 0xFF) else 0))
            val metadata = packetMetadata.getType(opcode)
            if (metadata == null) {
                if (ctx.channel().isOpen) {
                    ctx.channel().disconnect()
                    logger.fatal("Had to kick channel {} due to non-configured packet {} metadata.", ctx.channel(), opcode)
                }
                return
            }
            type = metadata

            when (type) {
                PacketType.IGNORE -> {
                    var length = packetMetadata.getLength(opcode)
                    if (length == -1) {
                        length = buf.readUnsignedByte().toInt()
                    } else if (length == -2) {
                        length = buf.readUnsignedShort()
                    }
                    buf.skipBytes(length)
                    setState(GameDecoderState.OPCODE)
                }
                PacketType.FIXED -> {
                    length = packetMetadata.getLength(opcode)
                    if (length == 0) {
                        setState(GameDecoderState.OPCODE)
                    } else {
                        setState(GameDecoderState.PAYLOAD)
                    }
                }
                PacketType.VARIABLE_BYTE, PacketType.VARIABLE_SHORT -> {
                    setState(GameDecoderState.LENGTH)
                }
                else -> {
                    throw IllegalStateException("Unhandled packet type $type for opcode $opcode.")
                }
            }
        }
    }

    private fun decodeLength(buf: ByteBuf) {
        if (buf.isReadable) {
            length = if (type == PacketType.VARIABLE_SHORT) buf.readUnsignedShort() else buf.readUnsignedByte().toInt()
            if (length != 0) {
                setState(GameDecoderState.PAYLOAD)
            }
        }
    }

    private fun decodePayload(buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() >= length) {
            val payload = buf.readBytes(length)
            setState(GameDecoderState.OPCODE)
            out.add(GamePacket(opcode, type, payload))
        }
    }

}