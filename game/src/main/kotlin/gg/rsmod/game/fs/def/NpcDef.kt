package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcDef(override val id: Int) : Definition(id) {

    var name = ""
    var size = 1
    var standAnim = -1
    var walkAnim = -1
    var render3 = -1
    var render4 = -1
    var render5 = -1
    var render6 = -1
    var render7 = -1
    var visibleMapDot = true
    var combatLevel = -1
    var width = -1
    var length = -1
    var render = false
    var headIcon = -1
    var varp = -1
    var varbit = -1
    var interactable = true
    var pet = false
    var options: Array<String?> = Array(5) { "" }
    var transforms: Array<Int>? = null

    var examine: String? = null

    fun isAttackable(): Boolean = combatLevel > 0 && options.any { it == "Attack" }

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                }
            }
            2 -> name = buf.readString()
            12 -> size = buf.readUnsignedByte().toInt()
            13 -> standAnim = buf.readUnsignedShort()
            14 -> walkAnim = buf.readUnsignedShort()
            15 -> render3 = buf.readUnsignedShort()
            16 -> render4 = buf.readUnsignedShort()
            17 -> {
                walkAnim = buf.readUnsignedShort()
                render5 = buf.readUnsignedShort()
                render6 = buf.readUnsignedShort()
                render7 = buf.readUnsignedShort()
            }
            in 30 until 35 -> {
                options[opcode - 30] = buf.readString()
                if (options[opcode - 30]?.toLowerCase() == "null") {
                    options[opcode - 30] = null
                }
            }
            40 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                    buf.readUnsignedShort()
                }
            }
            41 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                    buf.readUnsignedShort()
                }
            }
            60 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                }
            }
            93 -> visibleMapDot = false
            95 -> combatLevel = buf.readUnsignedShort()
            97 -> width = buf.readUnsignedShort()
            98 -> headIcon = buf.readUnsignedShort()
            99 -> render = true
            100 -> buf.readByte()
            101 -> buf.readByte()
            102 -> headIcon = buf.readUnsignedShort()
            103 -> buf.readUnsignedShort()
            106, 118 -> {
                varbit = buf.readUnsignedShort()
                varp = buf.readUnsignedShort()

                if (varbit == 65535) {
                    varbit = -1
                }
                if (varp == 65535) {
                    varp = -1
                }

                if (opcode == 118) {
                    buf.readUnsignedShort()
                }

                val count = buf.readUnsignedByte()

                transforms = Array(count.toInt() + 1) { 0 }
                for (i in 0..count) {
                    val transform = buf.readUnsignedShort()
                    transforms!![i] = transform
                }
            }
            107 -> interactable = false
            111 -> pet = true
            112 -> buf.readUnsignedByte()
            249 -> readParams(buf)
        }
    }
}