package gg.rsmod.game.protocol

import gg.rsmod.net.codec.login.LoginResultType
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * A simple implementation of [MessageToByteEncoder] that will write the id value
 * of [LoginResultType] for the client to read.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class HandshakeEncoder : MessageToByteEncoder<LoginResultType>() {

    override fun encode(ctx: ChannelHandlerContext, msg: LoginResultType, out: ByteBuf) {
        out.writeByte(msg.id)
    }
}