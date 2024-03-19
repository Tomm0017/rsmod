package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.IllegalStateException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemDef(override val id: Int) : Definition(id) {

    var name = ""
    var model = -1
    var stacks = false
    var cost = 0
    var members = false
    val groundMenu = Array<String?>(5) { null }
    val inventoryMenu = Array<String?>(5) { null }
    val equipmentMenu = Array<String?>(8) { null }
    /**
     * The item can be traded through the grand exchange.
     */
    var isTradable = false
    var dropOptionIndex = -2
    var teamCape = 0
    var ambient = 0
    var contrast = 0

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
    var notedId = -1
    var unnotedId = -1
    val params = Int2ObjectOpenHashMap<Any>()
    var category = -1
    var zoom2d = 0
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
    var attackSounds: IntArray? = null
    var skillReqs: Byte2ByteOpenHashMap? = null
    var equipSound: Int? = -1
    var femaleModel1 = 0
    var xan2d = 0
    var yan2d = 0
    var zan2d = 0
    var xOffset2d = 0
    var yOffset2d = 0
    var unknown1: String = ""
    var wearPos1: Int = -1
    var wearPos2: Int = -1
    var wearPos3: Int = -1
    var maleModel0: Int = 0
    var maleOffset: Int = 0
    var maleModel1: Int = 0

    var recolorSrc: IntArray = IntArray(0)
    var recolorDest: IntArray = IntArray(0)
    var retextureSrc: IntArray = IntArray(0)
    var retextureDest: IntArray = IntArray(0)

    var maleModel2: Int = 0
    var femaleModel2: Int = 0
    var maleHeadModel0: Int = 0
    var femaleHeadModel0: Int = 0
    var maleHeadModel1: Int = 0
    var femaleHeadModel1: Int = 0
    var countItem: IntArray = IntArray(0)
    var countCo: IntArray = IntArray(0)
    var resizeX: Int = 128
    var resizeY: Int = 128
    var resizeZ: Int = 128
    var femaleModel0 = 0
    var femaleOffset: Int = 0

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
            1 -> model = buf.readUnsignedShort()
            2 -> name = buf.readString()
            4 -> zoom2d = buf.readUnsignedShort()
            5 -> xan2d = buf.readUnsignedShort()
            6 -> yan2d = buf.readUnsignedShort()
            7 -> xOffset2d = buf.readUnsignedShort()
            8 -> yOffset2d = buf.readUnsignedShort()
            9 -> unknown1 = buf.readString()
            11 -> stacks = true
            12 -> cost = buf.readInt()
            13 -> wearPos1 = buf.readUnsignedByte().toInt()
            14 -> wearPos2 = buf.readUnsignedByte().toInt()
            16 -> members = true
            23 -> {
                maleModel0 = buf.readUnsignedShort()
                maleOffset = buf.readUnsignedByte().toInt()
            }
            24 -> maleModel1 = buf.readUnsignedShort()
            25 -> {
                 femaleModel0 = buf.readUnsignedShort()
                 femaleOffset = buf.readUnsignedByte().toInt()
            }
            26 -> femaleModel1 = buf.readUnsignedShort()
            27 -> wearPos3 = buf.readByte().toInt()
            in 30 until 35 -> {
                groundMenu[opcode - 30] = buf.readString()
                if (groundMenu[opcode - 30]!!.equals("null", true)
                    || groundMenu[opcode - 30]!!.equals("hidden", true)) {
                    groundMenu[opcode - 30] = null
                }
            }
            in 35 until 40 -> inventoryMenu[opcode - 35] = buf.readString()
            40,41 -> {
                val count = buf.readUnsignedByte().toInt()
                val src = IntArray(count)
                val dest = IntArray(count)
                repeat(count) {
                    src[it] = buf.readUnsignedShort()
                    src[it] = buf.readUnsignedShort()
                }
                if (opcode == 40) {
                    recolorSrc = src
                    recolorDest = dest
                } else {
                    retextureSrc = src
                    retextureDest = dest
                }
            }
            42 -> dropOptionIndex = buf.readByte().toInt()
            65 -> isTradable = true
            75 -> weight = buf.readShort().toDouble()
            78 -> maleModel2 = buf.readUnsignedShort()
            79 -> femaleModel2 = buf.readUnsignedShort()
            90 -> maleHeadModel0 = buf.readUnsignedShort()
            91 -> femaleHeadModel0 = buf.readUnsignedShort()
            92 -> maleHeadModel1 = buf.readUnsignedShort()
            93 -> femaleHeadModel1 = buf.readUnsignedShort()
            94 -> {
                category = buf.readUnsignedShort()
            }
            95 -> zan2d = buf.readUnsignedShort()
            97 -> noteLinkId = buf.readUnsignedShort()
            98 -> noteTemplateId = buf.readUnsignedShort()
            in 100 until 110 -> {
                if (countItem.isEmpty()) {
                    countItem = IntArray(10)
                    countCo = IntArray(10)
                }
                val index = opcode - 100
                countItem[index] = buf.readUnsignedShort()
                countCo[index] = buf.readUnsignedShort()
            }

            110 -> resizeX = buf.readUnsignedShort()
            111 -> resizeY = buf.readUnsignedShort()
            112 -> resizeZ = buf.readUnsignedShort()
            113 -> ambient = buf.readByte().toInt()
            114 -> contrast = buf.readByte().toInt() * 5
            115 -> teamCape = buf.readUnsignedByte().toInt()
            139 -> unnotedId = buf.readUnsignedShort()
            140 -> notedId = buf.readUnsignedShort()
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
            else -> throw IllegalStateException("Unknown opcode: $opcode in ItemDef")
        }
    }
}