package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.util.io.BufferUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginDecoder(private val serverRevision: Int) : StatefulFrameDecoder<LoginDecoderState>(LoginDecoderState.HANDSHAKE) {

    companion object {
        private val logger = LogManager.getLogger(LoginDecoder::class.java)

        private const val LOGIN_OPCODE = 16
        private const val RECONNECT_OPCODE = 18
    }

    private var payloadLength = -1

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>, state: LoginDecoderState) {
        buf.markReaderIndex()
        when (state) {
            LoginDecoderState.HANDSHAKE -> decodeHandshake(ctx, buf)
            LoginDecoderState.HEADER -> decodeHeader(ctx, buf, out)
        }
    }

    private fun decodeHandshake(ctx: ChannelHandlerContext, buf: ByteBuf) {
        if (buf.isReadable) {
            val opcode = buf.readByte().toInt()
            if (opcode == LOGIN_OPCODE || opcode == RECONNECT_OPCODE) {
                setState(LoginDecoderState.HEADER)
            } else {
                writeResponse(ctx, LoginResultType.BAD_SESSION_ID)
            }
        }
    }

    private fun decodeHeader(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() >= 3) {
            val size = buf.readUnsignedShort()
            if (buf.readableBytes() >= size) {
                val revision = buf.readInt()
                buf.skipBytes(Int.SIZE_BYTES)
                buf.skipBytes(Byte.SIZE_BYTES)
                if (revision == serverRevision) {
                    payloadLength = size - (Int.SIZE_BYTES + Int.SIZE_BYTES + Byte.SIZE_BYTES)
                    decodePayload(ctx, buf, out)
                } else {
                    writeResponse(ctx, LoginResultType.REVISION_MISMATCH)
                }
            } else {
                buf.resetReaderIndex()
            }
        }
    }

    private fun decodePayload(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() >= payloadLength) {
            val isaacSeed = IntArray(4)

            buf.skipBytes(Byte.SIZE_BYTES)
            for (i in 0 until 4) {
                isaacSeed[i] = buf.readInt()
            }
            buf.skipBytes(Long.SIZE_BYTES)

            val authRequest = buf.readByte().toInt()
            var authCode = -1
            if (authRequest == 0 || authRequest == 3) {
                authCode = buf.readUnsignedMedium()
            } else {
                buf.skipBytes(Int.SIZE_BYTES)
            }

            buf.skipBytes(Byte.SIZE_BYTES)
            val password = BufferUtils.readString(buf)
            val username = BufferUtils.readString(buf)

            val clientSettings = buf.readByte().toInt()
            val clientResizable = (clientSettings shr 1) == 1
            val clientWidth = buf.readUnsignedShort()
            val clientHeight = buf.readUnsignedShort()

            buf.skipBytes(24) // random.dat data
            BufferUtils.readString(buf)
            buf.skipBytes(4)

            buf.skipBytes(17)
            BufferUtils.readJagexString(buf)
            BufferUtils.readJagexString(buf)
            BufferUtils.readJagexString(buf)
            BufferUtils.readJagexString(buf)
            buf.skipBytes(3)
            BufferUtils.readJagexString(buf)
            BufferUtils.readJagexString(buf)
            buf.skipBytes(2)
            buf.skipBytes(4 * 3)
            buf.skipBytes(4)
            buf.skipBytes(1)
            buf.skipBytes(Int.SIZE_BYTES)

            val crcs = IntArray(18)
            for (i in 0 until crcs.size) {
                crcs[i] = buf.readInt()
            }

            logger.info("User '{}' login request from {}.", username, ctx.channel())

            val request = LoginRequest(channel = ctx.channel(), username = username,
                    password = password, revision = serverRevision, isaacSeed = isaacSeed,
                    crcs = crcs, resizableClient = clientResizable, auth = authCode,
                    uuid = "".toUpperCase())
            out.add(request)
        }
    }

    private fun writeResponse(ctx: ChannelHandlerContext, result: LoginResultType) {
        val buf = ctx.channel().alloc().buffer(1)
        buf.writeByte(result.id)
        ctx.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
    }
}