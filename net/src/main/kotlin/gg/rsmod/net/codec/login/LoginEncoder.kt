package gg.rsmod.net.codec.login

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginEncoder : MessageToByteEncoder<LoginResponse>() {

    override fun encode(ctx: ChannelHandlerContext, player: LoginResponse, out: ByteBuf) {
        with(out) {
            writeByte(2) // Minutes for "You have only just left another world.", "Your profile will be transferred in:", field442 / 60 + " seconds." field442 will be this byte.
            writeByte(37) // playerUUIDLength
            writeByte(0) // boolean that gets either way assigned 1
            writeInt(0) // clientPreferences
            writeByte(player.privilege)  // staffModLevel
            writeBoolean (true) // playerMod
            writeShort(player.index) // localPlayerIndex
            writeBoolean(true) // Expand friends/ignore list [Membership perk] => If player is member expand it by 100 or w.e it was on osrs
            writeLong(0) // accountHash
            writeLong(0) // playerUUID
            writeLong(0)
        }
    }
}