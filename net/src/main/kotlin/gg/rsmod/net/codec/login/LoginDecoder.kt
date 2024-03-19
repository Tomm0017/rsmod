package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.util.io.BufferUtils.readIntIME
import gg.rsmod.util.io.BufferUtils.readJagexString
import gg.rsmod.util.io.BufferUtils.readIntLE
import gg.rsmod.util.io.BufferUtils.readIntME
import gg.rsmod.util.io.BufferUtils.readString
import gg.rsmod.util.io.Xtea
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import java.math.BigInteger

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

    private fun ChannelHandlerContext.writeResponse(result: LoginResultType) {
        val buf = channel().alloc().buffer(1)
        buf.writeByte(result.id)
        writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
    }

    private fun decodeHeader(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() >= 3) {
            val size = buf.readUnsignedShort() // always 0
            if (buf.readableBytes() >= size) {

                val revision
                    = buf.readInt()
                buf.readInt() // always 1
                buf.readUnsignedByte() // client type
                /**
                 * login protocols see param4 sent before and inside the xtea buffer
                 * and clientType here is an addition since the inclusion of mobile;
                 * all of this is ignored by rsmod for now.
                 */
                buf.readUnsignedByte() // param4 is written as a signed byte

                if (revision == serverRevision) {
                    payloadLength = size - (Int.SIZE_BYTES + Int.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES)
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
            buf.readUnsignedByte() // The byte that you commented out.

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

            val xteaKeys = IntArray(4) {secureBuf.readInt()}
            val reportedSeed = secureBuf.readLong()

            var authCode: Int = -1
            val password: String?
            val previousXteaKeys = IntArray(4)

            if (reconnecting) {
                for (i in 0 until previousXteaKeys.size) {
                    previousXteaKeys[i] = secureBuf.readInt()
                }

                password = null
            } else {
                when(secureBuf.readByte().toInt()) {
                    0,1 -> {
                        authCode = secureBuf.readUnsignedMedium()
                        secureBuf.skipBytes(Byte.SIZE_BYTES)
                    }
                    2 -> secureBuf.skipBytes(Int.SIZE_BYTES)
                    3 -> authCode = secureBuf.readInt()
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
            xteaBuf.readString() // param9
            xteaBuf.skipBytes(Int.SIZE_BYTES) // param14
            xteaBuf.skipBytes(55) //platform info block size - rev 211
            xteaBuf.readByte() // client type
            xteaBuf.skipBytes(Int.SIZE_BYTES) // 0


            /**
             * For now ignored since 1) Busy with other stuff. 2) Not that important.
             */
//            val crcs = decodeCRCs(xteaBuf)
//            for (i in crcs.indices) {
//                println("$i ${crcs[i]}")
//                /**
//                 * CRC for index 16 is always sent as 0 (at least on the
//                 * Desktop client, need to look into mobile).
//                 */
//                if (i == 16) {
//                    continue
//                }
////                if (!cacheCrcs.contains(crcs[i])) {
////                    buf.resetReaderIndex()
////                    buf.skipBytes(payloadLength)
////                    logger.info { "User '$username' login request crc mismatch [requestCrc=${crcs.contentToString()}, cacheCrc=${cacheCrcs.contentToString()}]." }
////                    println("$i : ${crcs[i]}  != ${cacheCrcs[i]}")
////
////                    ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
////                    return
////                }
//            }

            logger.info { "User '$username' login request from ${ctx.channel()}." }

            val request = LoginRequest(channel = ctx.channel(), username = username,
                password = password ?: "", revision = serverRevision, xteaKeys = xteaKeys,
                resizableClient = clientResizable, auth = authCode, uuid = "".lowercase(), clientWidth = clientWidth, clientHeight = clientHeight,
                reconnecting = reconnecting)
            out.add(request)
        }
    }

    private fun ByteBuf.decipher(xteaKeys: IntArray): ByteBuf {
        val data = ByteArray(readableBytes())
        readBytes(data)
        return Unpooled.wrappedBuffer(Xtea.decipher(xteaKeys, data, 0, data.size))
    }
    /**
     * switch based on incoming CRCorder
     */
    private fun decodeCRCs(xteaBuf: ByteBuf): IntArray {
        val crcs = IntArray(cacheCrcs.size)
        for(i in CRCorder.indices){
            when(val idx = CRCorder[i]){
                9,17,10,13,6,12,5,8,1 -> crcs[idx] = xteaBuf.readIntME()
                20,11,3,15,19 -> crcs[idx] = xteaBuf.readIntLE()
                18,4,7 -> crcs[idx] = xteaBuf.readInt()
                0,2,14 -> crcs[idx] = xteaBuf.readIntIME()
            }
        }

        return crcs
    }

    /**
     * As of revision 190 the client now sends the CRCs out of order
     * and with varying byte orders
     */
     companion object {
         private val logger = KotlinLogging.logger{}
         private const val LOGIN_OPCODE = 16
         private const val RECONNECT_OPCODE = 18
         private val CRCorder = intArrayOf(
             9,20,17,10,13,6,12,18,11,5,3,4,0,2,14,15,19,8,1,7
         )
    }
}
