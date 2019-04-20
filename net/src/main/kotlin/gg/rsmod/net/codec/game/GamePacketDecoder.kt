package gg.rsmod.net.codec.game

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.IPacketMetadata
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.io.IsaacRandom
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import mu.KLogging

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GamePacketDecoder(private val random: IsaacRandom?, private val packetMetadata: IPacketMetadata) : StatefulFrameDecoder<GameDecoderState>(GameDecoderState.OPCODE) {

    private var opcode = 0

    private var length = 0

    private var type = PacketType.FIXED

    private var ignore = false

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>, state: GameDecoderState) {
        when (state) {
            GameDecoderState.OPCODE -> decodeOpcode(ctx, buf, out)
            GameDecoderState.LENGTH -> decodeLength(buf, out)
            GameDecoderState.PAYLOAD -> decodePayload(buf, out)
        }
    }

    private fun decodeOpcode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.isReadable) {
            opcode = buf.readUnsignedByte().toInt() - (random?.nextInt() ?: 0) and 0xFF
            val packetType = packetMetadata.getType(opcode)
            if (packetType == null) {
                logger.warn("Channel {} sent message with no valid metadata: {}.", ctx.channel(), opcode)
                buf.skipBytes(buf.readableBytes())
                return
            }
            type = packetType
            ignore = packetMetadata.shouldIgnore(opcode)

            when (type) {
                PacketType.FIXED -> {
                    length = packetMetadata.getLength(opcode)
                    if (length != 0) {
                        setState(GameDecoderState.PAYLOAD)
                    } else if (!ignore) {
                        out.add(GamePacket(opcode, type, Unpooled.EMPTY_BUFFER))
                    }
                }
                PacketType.VARIABLE_BYTE, PacketType.VARIABLE_SHORT -> setState(GameDecoderState.LENGTH)
                else -> throw IllegalStateException("Unhandled packet type $type for opcode $opcode.")
            }
        }
    }

    private fun decodeLength(buf: ByteBuf, out: MutableList<Any>) {
        if (buf.isReadable) {
            length = if (type == PacketType.VARIABLE_SHORT) buf.readUnsignedShort() else buf.readUnsignedByte().toInt()
            if (length != 0) {
                setState(GameDecoderState.PAYLOAD)
            } else if (!ignore) {
                out.add(GamePacket(opcode, type, Unpooled.EMPTY_BUFFER))
            }
        }
    }

    private fun decodePayload(buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() >= length) {
            val payload = buf.readBytes(length)
            setState(GameDecoderState.OPCODE)

            /**
             * If the packet isn't flagged as being a packet we should ignore,
             * we queue it up for our game to process the packet.
             */
            if (!ignore) {
                out.add(GamePacket(opcode, type, payload))
            }
        }
    }

    companion object : KLogging()
}
