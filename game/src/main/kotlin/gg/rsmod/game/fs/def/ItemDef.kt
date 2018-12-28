package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemDef(override val id: Int) : Definition(id) {

    lateinit var name: String
    var stackable = false
    var cost = 0
    var members = false
    val groundMenu = Array<String?>(5) { null }
    val inventoryMenu = Array<String?>(5) { null }
    var tradeable = false
    var teamCape = 0
    /**
     * When an item is noted or unnoted (and has a noted variant), this will
     * represent the other item id. For example, item definition [4151] will
     * have a [noteLinkId] of [4152], while item definition [4152] will have
     * a [noteLinkId] of 4151.
     */
    var noteLinkId = 0
    /**
     * When an item is noted, it will set this value.
     */
    var noteTemplateId = 0
    var placeholderId = 0
    var placeholderTemplateId = 0

    fun isStackable(): Boolean = stackable || noteTemplateId > 0

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> buf.readUnsignedShort()
            2 -> name = BufferUtils.readString(buf)
            4 -> buf.readUnsignedShort()
            5 -> buf.readUnsignedShort()
            6 -> buf.readUnsignedShort()
            7 -> buf.readUnsignedShort()
            8 -> buf.readUnsignedShort()
            11 -> stackable = true
            12 -> cost = buf.readInt()
            16 -> members = true
            23 -> {
                buf.readUnsignedShort()
                buf.readUnsignedByte()
            }
            24 -> buf.readUnsignedShort()
            25 -> {
                buf.readUnsignedShort()
                buf.readUnsignedByte()
            }
            26 -> buf.readUnsignedShort()
            in 30 until 35 -> {
                groundMenu[opcode - 30] = BufferUtils.readString(buf)
                if (groundMenu[opcode - 30]!!.toLowerCase() == "null") {
                    groundMenu[opcode - 30] = null
                }
            }
            in 35 until 40 -> inventoryMenu[opcode - 35] = BufferUtils.readString(buf)
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
            42 -> buf.readByte()
            65 -> tradeable = true
            78 -> buf.readUnsignedShort()
            79 -> buf.readUnsignedShort()
            90 -> buf.readUnsignedShort()
            91 -> buf.readUnsignedShort()
            92 -> buf.readUnsignedShort()
            93 -> buf.readUnsignedShort()
            95 -> buf.readUnsignedShort()
            97 -> noteLinkId = buf.readUnsignedShort()
            98 -> noteTemplateId = buf.readUnsignedShort()
            in 100 until 110 -> {
                buf.readUnsignedShort()
                buf.readUnsignedShort()
            }
            110 -> buf.readUnsignedShort()
            111 -> buf.readUnsignedShort()
            112 -> buf.readUnsignedShort()
            113 -> buf.readByte()
            114 -> buf.readByte()
            115 -> teamCape = buf.readUnsignedByte().toInt()
            139 -> buf.readUnsignedShort()
            140 -> buf.readUnsignedShort()
            148 -> placeholderId = buf.readUnsignedShort()
            149 -> placeholderTemplateId = buf.readUnsignedShort()
            249 -> readParams(buf)
        }
    }
}