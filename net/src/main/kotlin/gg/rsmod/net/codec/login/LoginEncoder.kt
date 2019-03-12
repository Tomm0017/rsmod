package gg.rsmod.net.codec.login

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginEncoder : MessageToByteEncoder<LoginResponse>() {

    override fun encode(ctx: ChannelHandlerContext, msg: LoginResponse, out: ByteBuf) {
        out.writeByte(2)
        out.writeByte(13)
        out.writeByte(0)
        out.writeInt(0)
        out.writeByte(msg.privilege)
        out.writeBoolean(true) // members
        out.writeShort(msg.index)
        out.writeBoolean(true) // members
    }
}