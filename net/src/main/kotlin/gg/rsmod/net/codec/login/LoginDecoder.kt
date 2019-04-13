package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.util.io.BufferUtils.readJagexString
import gg.rsmod.util.io.BufferUtils.readString
import gg.rsmod.util.io.Xtea
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import java.math.BigInteger
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginDecoder(private val serverRevision: Int, private val cacheCrcs: IntArray,
                   private val serverSeed: Long, private val rsaExponent: BigInteger?, private val rsaModulus: BigInteger?) : StatefulFrameDecoder<LoginDecoderState>(LoginDecoderState.HANDSHAKE) {

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
                ctx.writeResponse(LoginResultType.BAD_SESSION_ID)
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
                    ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
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
                ctx.writeResponse(LoginResultType.BAD_SESSION_ID)
                return
            }

            val xteaKeys = IntArray(4) { secureBuf.readInt() }
            val reportedSeed = secureBuf.readLong()

            val authCode: Int
            val password: String?
            val previousXteaKeys = IntArray(4)

            if (reconnecting) {
                for (i in 0 until previousXteaKeys.size) {
                    previousXteaKeys[i] = secureBuf.readInt()
                }

                authCode = -1
                password = null
            } else {
                val authType = secureBuf.readByte().toInt()

                if (authType == 1) {
                    authCode = secureBuf.readInt()
                } else if (authType == 0 || authType == 2) {
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
                ctx.writeResponse(LoginResultType.BAD_SESSION_ID)
                return
            }

            val clientSettings = xteaBuf.readByte().toInt()
            val clientResizable = (clientSettings shr 1) == 1
            val clientWidth = xteaBuf.readUnsignedShort()
            val clientHeight = xteaBuf.readUnsignedShort()

            xteaBuf.skipBytes(24) // random.dat data
            xteaBuf.readString()
            xteaBuf.skipBytes(Int.SIZE_BYTES)

            xteaBuf.skipBytes(Byte.SIZE_BYTES * 9)
            xteaBuf.skipBytes(Short.SIZE_BYTES)
            xteaBuf.skipBytes(Byte.SIZE_BYTES)
            xteaBuf.skipBytes(Byte.SIZE_BYTES * 3)
            xteaBuf.skipBytes(Short.SIZE_BYTES)
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.skipBytes(Byte.SIZE_BYTES)
            xteaBuf.skipBytes(Short.SIZE_BYTES)
            xteaBuf.readJagexString()
            xteaBuf.readJagexString()
            xteaBuf.skipBytes(Byte.SIZE_BYTES * 2)
            xteaBuf.skipBytes(Int.SIZE_BYTES * 3)
            xteaBuf.skipBytes(Int.SIZE_BYTES)
            xteaBuf.readJagexString()

            xteaBuf.skipBytes(Int.SIZE_BYTES * 3)

            val crcs = IntArray(cacheCrcs.size) { xteaBuf.readInt() }

            for (i in 0 until crcs.size) {
                /**
                 * CRC for index 16 is always sent as 0 (at least on the
                 * Desktop client, need to look into mobile).
                 */
                if (i == 16) {
                    continue
                }
                if (crcs[i] != cacheCrcs[i]) {
                    buf.resetReaderIndex()
                    buf.skipBytes(payloadLength)
                    logger.info { "User '$username' login request crc mismatch [requestCrc=${Arrays.toString(crcs)}, cacheCrc=${Arrays.toString(cacheCrcs)}]." }
                    ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
                    return
                }
            }

            logger.info { "User '$username' login request from ${ctx.channel()}." }

            val request = LoginRequest(channel = ctx.channel(), username = username,
                    password = password ?: "", revision = serverRevision, xteaKeys = xteaKeys,
                    resizableClient = clientResizable, auth = authCode, uuid = "".toUpperCase(), clientWidth = clientWidth, clientHeight = clientHeight,
                    reconnecting = reconnecting)
            out.add(request)
        }
    }

    private fun ChannelHandlerContext.writeResponse(result: LoginResultType) {
        val buf = channel().alloc().buffer(1)
        buf.writeByte(result.id)
        writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
    }

    private fun ByteBuf.decipher(xteaKeys: IntArray): ByteBuf {
        val data = ByteArray(readableBytes())
        readBytes(data)
        return Unpooled.wrappedBuffer(Xtea.decipher(xteaKeys, data, 0, data.size))
    }

    companion object: KLogging() {
        private const val LOGIN_OPCODE = 16
        private const val RECONNECT_OPCODE = 18
    }
}
