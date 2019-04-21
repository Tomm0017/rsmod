package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemDef(override val id: Int) : Definition(id) {

    var name = ""
    var stacks = false
    var cost = 0
    var members = false
    val groundMenu = Array<String?>(5) { null }
    val inventoryMenu = Array<String?>(5) { null }
    val equipmentMenu = Array<String?>(8) { null }
    /**
     * The item can be traded through the grand exchange.
     */
    var grandExchange = false
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
    var placeholderLink = 0
    var placeholderTemplate = 0

    val params = Int2ObjectOpenHashMap<Any>()

    /**
     * Custom metadata.
     */
    var examine: String? = null
    var tradeable = false
    var weight = 0.0
    var attackSpeed = -1
    var equipSlot = -1
    var equipType = 0
    var weaponType = -1
    var renderAnimations: IntArray? = null
    var skillReqs: Byte2ByteOpenHashMap? = null
    lateinit var bonuses: IntArray

    val stackable: Boolean
        get() = stacks || noteTemplateId > 0

    val noted: Boolean
        get() = noteTemplateId > 0

    /**
     * Whether or not the object is a placeholder.
     */
    val isPlaceholder
        get() = placeholderTemplate > 0 && placeholderLink > 0

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> buf.readUnsignedShort()
            2 -> name = buf.readString()
            4 -> buf.readUnsignedShort()
            5 -> buf.readUnsignedShort()
            6 -> buf.readUnsignedShort()
            7 -> buf.readUnsignedShort()
            8 -> buf.readUnsignedShort()
            11 -> stacks = true
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
                groundMenu[opcode - 30] = buf.readString()
                if (groundMenu[opcode - 30]!!.toLowerCase() == "null") {
                    groundMenu[opcode - 30] = null
                }
            }
            in 35 until 40 -> inventoryMenu[opcode - 35] = buf.readString()
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
            65 -> grandExchange = true
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
            148 -> placeholderLink = buf.readUnsignedShort()
            149 -> placeholderTemplate = buf.readUnsignedShort()
            249 -> {
                params.putAll(readParams(buf))

                for (i in 0 until 8) {
                    val paramId = 451 + i
                    val option = params.get(paramId) as? String ?: continue
                    equipmentMenu[i] = option
                }
            }
        }
    }
}