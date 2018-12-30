package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class VarbitDef(override val id: Int) : Definition(id) {

    var varp = 0
    var startBit = 0
    var endBit = 0

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                varp = buf.readUnsignedShort()
                startBit = buf.readUnsignedByte().toInt()
                endBit = buf.readUnsignedByte().toInt()
            }
        }
    }
}