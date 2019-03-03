package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.util.io.BufferUtils.readJagexString
import gg.rsmod.util.io.BufferUtils.readString
import gg.rsmod.util.io.Xtea
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

    private var reconnecting = false

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
                reconnecting = opcode == RECONNECT_OPCODE
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

            val xteaKeys = IntArray(4) { secureBuf.readInt() }
            val reportedSeed = secureBuf.readLong()

            val authType: Int
            val authCode: Int
            val password: String?
            val previousXteaKeys = IntArray(4)

            if (reconnecting) {
                for (i in 0 until previousXteaKeys.size) {
                    previousXteaKeys[i] = secureBuf.readInt()
                }

                authType = -1
                authCode = -1
                password = null
            } else {
                authType = secureBuf.readByte().toInt()

                if (authType == 0) {
                    authCode = secureBuf.readInt()
                } else if (authType == 1 || authType == 3) {
                    authCode = secureBuf.readUnsignedMedium()
                    secureBuf.skipBytes(Byte.SIZE_BYTES)
                } else {
                    authCode = secureBuf.readInt()
                }

                secureBuf.skipBytes(Byte.SIZE_BYTES)
                password = secureBuf.readString()
            }

            val xteaBuf = buf.decipher(xteaKeys)
            val username = xteaBuf.readString()

            if (reportedSeed != serverSeed) {
                xteaBuf.resetReaderIndex()
                xteaBuf.skipBytes(payloadLength)
                logger.info("User '{}' login request seed mismatch [receivedSeed=$reportedSeed, expectedSeed=$serverSeed].", username, reportedSeed, serverSeed)
                writeResponse(ctx, LoginResultType.BAD_SESSION_ID)
                return
            }

            val clientSettings = xteaBuf.readByte().toInt()
            val clientResizable = (clientSettings shr 1) == 1
            val clientLowMem = (clientSettings and 1) == 1
            val clientWidth = xteaBuf.readUnsignedShort()
            val clientHeight = xteaBuf.readUnsignedShort()

            xteaBuf.skipBytes(24) // random.dat data
            xteaBuf.readString()
            xteaBuf.skipBytes(4)

            xteaBuf.skipBytes(17)
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.skipBytes(3)
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.skipBytes(2)
            xteaBuf.skipBytes(4 * 3)
            xteaBuf.skipBytes(4)
            xteaBuf.skipBytes(1)
            xteaBuf.skipBytes(Int.SIZE_BYTES)

            val crcs = IntArray(18) { xteaBuf.readInt() }

            logger.info { "User '$username' login request from ${ctx.channel()}." }

            // TODO: reconnect without password
            val request = LoginRequest(channel = ctx.channel(), username = username,
                    password = password ?: "", revision = serverRevision, xteaKeys = xteaKeys,
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

    private fun ByteBuf.decipher(xteaKeys: IntArray): ByteBuf {
        val data = ByteArray(readableBytes())
        readBytes(data)
        return Unpooled.wrappedBuffer(Xtea.decipher(xteaKeys, data, 0, data.size))
    }
}