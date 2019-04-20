package gg.rsmod.net.codec.filestore

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.net.codec.login.LoginResultType
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import mu.KLogging

/**
 * @author Tom <rspsmods@gmail.com>
 */
class FilestoreDecoder(private val serverRevision: Int) : StatefulFrameDecoder<FilestoreDecoderState>(FilestoreDecoderState.REVISION_REQUEST) {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>, state: FilestoreDecoderState) {
        when (state) {
            FilestoreDecoderState.REVISION_REQUEST -> decodeRevisionRequest(ctx, buf)
            FilestoreDecoderState.ARCHIVE_REQUEST -> decodeArchiveRequest(buf, out)
        }
    }

    private fun decodeRevisionRequest(ctx: ChannelHandlerContext, buf: ByteBuf) {
        if (buf.readableBytes() >= 4) {
            val revision = buf.readInt()
            if (revision != serverRevision) {
                logger.info("Revision mismatch for channel {} with client revision {} when expecting {}.", ctx.channel(), revision, serverRevision)
                ctx.writeAndFlush(LoginResultType.REVISION_MISMATCH).addListener(ChannelFutureListener.CLOSE)
            } else {
                setState(FilestoreDecoderState.ARCHIVE_REQUEST)
                ctx.writeAndFlush(LoginResultType.ACCEPTABLE)
            }
        }
    }

    private fun decodeArchiveRequest(buf: ByteBuf, out: MutableList<Any>) {
        if (!buf.isReadable) {
            return
        }
        buf.markReaderIndex()
        val opcode = buf.readByte().toInt()
        when (opcode) {
            CLIENT_INIT_GAME, CLIENT_LOAD_SCREEN, CLIENT_INIT_OPCODE -> {
                buf.skipBytes(3)
            }
            ARCHIVE_REQUEST_NEUTRAL, ARCHIVE_REQUEST_URGENT -> {
                if (buf.readableBytes() >= 3) {
                    val index = buf.readUnsignedByte().toInt()
                    val archive = buf.readUnsignedShort()

                    val request = FilestoreRequest(index = index, archive = archive, priority = opcode == ARCHIVE_REQUEST_URGENT)
                    out.add(request)
                } else {
                    buf.resetReaderIndex()
                }
            }
            else -> {
                logger.error("Unhandled opcode: $opcode")
            }
        }
    }

    companion object : KLogging() {
        private const val ARCHIVE_REQUEST_URGENT = 0
        private const val ARCHIVE_REQUEST_NEUTRAL = 1
        private const val CLIENT_INIT_GAME = 2
        private const val CLIENT_LOAD_SCREEN = 3
        private const val CLIENT_INIT_OPCODE = 6
    }
}
