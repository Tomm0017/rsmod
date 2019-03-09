package gg.rsmod.game.fs

import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * A [Definition] is used to represent metadata that can be decoded from the
 * game resources.
 *
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

    fun readParams(buf: ByteBuf): Int2ObjectOpenHashMap<Any> {
        val map = Int2ObjectOpenHashMap<Any>()

        val length = buf.readUnsignedByte()
        for (i in 0 until length) {
            val isString = buf.readUnsignedByte().toInt() == 1
            val id = buf.readUnsignedMedium()
            if (isString) {
                map[id] = buf.readString()
            } else {
                map[id] = buf.readInt()
            }
        }
        return map
    }
}