package gg.rsmod.game.fs

import gg.rsmod.util.BufferUtils
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Definition(open val id: Int) {

    fun decode(buf: ByteBuf) {
        while (true) {
            val opcode = buf.readUnsignedByte().toInt()
            if (opcode == 0) {
                break
            }
            decode(buf, opcode)
        }
    }

    abstract fun decode(buf: ByteBuf, opcode: Int)

    fun readParams(buf: ByteBuf) {
        val length = buf.readUnsignedByte()
        for (i in 0 until length) {
            val isString = buf.readUnsignedByte().toInt() == 1
            buf.readUnsignedMedium()
            if (isString) {
                BufferUtils.readString(buf)
            } else {
                buf.readInt()
            }
        }
    }
}