package gg.rsmod.game.protocol

import gg.rsmod.net.codec.filestore.FilestoreDecoder
import gg.rsmod.net.codec.filestore.FilestoreEncoder
import gg.rsmod.net.codec.handshake.HandshakeMessage
import gg.rsmod.net.codec.handshake.HandshakeType
import gg.rsmod.net.codec.login.LoginDecoder
import gg.rsmod.net.codec.login.LoginEncoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import mu.KLogging
import java.math.BigInteger

/**
 * A [ByteToMessageDecoder] implementation which is responsible for handling
 * the initial handshake signal from the client. The implementation is dependant
 * on the network module.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class HandshakeDecoder(private val revision: Int, private val cacheCrcs: IntArray, private val rsaExponent: BigInteger?,
                       private val rsaModulus: BigInteger?) : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (!buf.isReadable) {
            return
        }

        val opcode = buf.readByte().toInt()
        val handshake = HandshakeType.values.firstOrNull { it.id == opcode }
        when (handshake) {
            HandshakeType.FILESTORE -> {
                val p = ctx.pipeline()
                p.addFirst("filestore_encoder", FilestoreEncoder())
                p.addAfter("handshake_decoder", "filestore_decoder", FilestoreDecoder(revision))
            }
            HandshakeType.LOGIN -> {
                val p = ctx.pipeline()
                val serverSeed = (Math.random() * Long.MAX_VALUE).toLong()

                p.addFirst("login_encoder", LoginEncoder())
                p.addAfter("handshake_decoder", "login_decoder", LoginDecoder(revision, cacheCrcs, serverSeed, rsaExponent, rsaModulus))

                ctx.writeAndFlush(ctx.alloc().buffer(1).writeByte(0))
                ctx.writeAndFlush(ctx.alloc().buffer(8).writeLong(serverSeed))
            }
            else -> {
                /*
                 * If the handshake type is not handled, we want to log it and
                 * make sure we read any bytes from the buffer.
                 */
                buf.readBytes(buf.readableBytes())
                logger.warn("Unhandled handshake type {} requested by {}.", opcode, ctx.channel())
                return
            }
        }
        /*
         * This decoder is no longer needed for this context, so we discard it.
         */
        ctx.pipeline().remove(this)
        out.add(HandshakeMessage(handshake.id))
    }

    companion object : KLogging()
}
