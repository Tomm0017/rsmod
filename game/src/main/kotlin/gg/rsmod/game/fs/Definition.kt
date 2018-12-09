package gg.rsmod.game.fs

import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface Definition {

    fun decode(buf: ByteBuf) {
        while (true) {
            val opcode = buf.readUnsignedByte().toInt()
            if (opcode == 0) {
                break
            }
            decode(buf, opcode)
        }
    }

    @Throws(RuntimeException::class)
    fun decode(buf: ByteBuf, opcode: Int)
}