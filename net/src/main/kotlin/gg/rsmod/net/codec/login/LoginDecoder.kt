package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.util.io.readJagexString
import gg.rsmod.util.io.readString
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import mu.KotlinLogging
import java.math.BigInteger

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginDecoder(private val serverRevision: Int, private val rsaExponent: BigInteger?,
                   private val rsaModulus: BigInteger?, private val serverSeed: Long) : StatefulFrameDecoder<LoginDecoderState>(LoginDecoderState.HANDSHAKE) {

    companion object {
        private val logger = KotlinLogging.logger {  }

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
                buf.skipBytes(Int.SIZE_BYTES) // Always 1
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
            buf.markReaderIndex()

            val secureBuf: ByteBuf = if (rsaExponent != null && rsaModulus != null) {
                val secureBufLength = buf.readUnsignedShort()
                val secureBuf = buf.readBytes(secureBufLength)
                val rsaValue = BigInteger(secureBuf.array()).modPow(rsaExponent, rsaModulus)
                Unpooled.wrappedBuffer(rsaValue.toByteArray())
            } else {
                buf
            }

            val successfulEncryption = secureBuf.readUnsignedByte().toInt() == 1
            if (!successfulEncryption) {
                buf.resetReaderIndex()
                buf.skipBytes(payloadLength)
                logger.info("Channel '{}' login request rejected.", ctx.channel())
                writeResponse(ctx, LoginResultType.BAD_SESSION_ID)
                return
            }

            val clientSeed = IntArray(4)
            for (i in 0 until clientSeed.size) {
                clientSeed[i] = secureBuf.readInt()
            }
            val reportedSeed = secureBuf.readLong()

            var authCode = -1
            val authRequest = secureBuf.readByte().toInt()
            if (authRequest == 1 && authRequest == 3) {
                authCode = secureBuf.readUnsignedMedium()
            } else if (authRequest == 0) {
                secureBuf.skipBytes(Int.SIZE_BYTES) // some info from 2fa
            } else {
                secureBuf.skipBytes(Int.SIZE_BYTES)
            }

            secureBuf.skipBytes(Byte.SIZE_BYTES)

            val password = secureBuf.readString()
            val username = buf.readString()

            if (reportedSeed != serverSeed) {
                buf.resetReaderIndex()
                buf.skipBytes(payloadLength)
                logger.info("User '{}' login request seed mismatch [receivedSeed=$reportedSeed, expectedSeed=$serverSeed].", username, reportedSeed, serverSeed)
                writeResponse(ctx, LoginResultType.BAD_SESSION_ID)
                return
            }

            val clientSettings = buf.readByte().toInt()
            val clientResizable = (clientSettings shr 1) == 1
            val clientWidth = buf.readUnsignedShort()
            val clientHeight = buf.readUnsignedShort()

            buf.skipBytes(24) // random.dat data
            buf.readString()
            buf.skipBytes(4)

            buf.skipBytes(17)
            buf.readJagexString()
            buf.readJagexString()
            buf.readJagexString()
            buf.readJagexString()
            buf.skipBytes(3)
            buf.readJagexString()
            buf.readJagexString()
            buf.skipBytes(2)
            buf.skipBytes(4 * 3)
            buf.skipBytes(4)
            buf.skipBytes(1)
            buf.skipBytes(Int.SIZE_BYTES)

            val crcs = IntArray(18)
            for (i in 0 until crcs.size) {
                crcs[i] = buf.readInt()
            }

            val isaacSeed = IntArray(4)
            isaacSeed[0] = clientSeed[0]
            isaacSeed[1] = clientSeed[1]
            isaacSeed[2] = serverSeed.toInt() shr 32
            isaacSeed[3] = serverSeed.toInt()

            logger.info("User '{}' login request from {}.", username, ctx.channel())

            val request = LoginRequest(channel = ctx.channel(), username = username,
                    password = password, revision = serverRevision, isaacSeed = isaacSeed,
                    crcs = crcs, resizableClient = clientResizable, auth = authCode,
                    uuid = "".toUpperCase(), clientWidth = clientWidth, clientHeight = clientHeight)
            out.add(request)
        }
    }

    private fun writeResponse(ctx: ChannelHandlerContext, result: LoginResultType) {
        val buf = ctx.channel().alloc().buffer(1)
        buf.writeByte(result.id)
        ctx.writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
    }
}