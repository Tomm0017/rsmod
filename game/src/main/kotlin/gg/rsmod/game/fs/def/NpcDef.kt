package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.IllegalStateException

/**
 * @author Tom <rspsmods@gmail.com>
 */
private val DEFAULT_HEAD_ICONS = emptyArray<Int>()

class NpcDef(override val id: Int) : Definition(id) {

    var name = ""
    var models: Array<Int> = emptyArray<Int>()
    var category = -1
    var size = 1
    var standAnim = -1
    var walkAnim = -1
    var rotateLeftAnim = -1
    var rotateRightAnim = -1
    var rotateBackAnim = -1
    var walkLeftAnim = -1
    var walkRightAnim = -1
    var isMinimapVisible = true
    var combatLevel = -1
    var widthScale = -1
    var heightScale = -1
    var length = -1
    var renderPriority = false
    var ambient = 0
    var contrast = 0
    var rotation = 0
    var headIcon = -1
    var transformVarp = -1
    var transformVarbit = -1
    var defaultTransform = -1
    var interactable = true
    var clickable = true
    var lowPriorityFollowerOps = false
    var isPet = false
    var runAnimation = -1

    var runRotate180Animation = -1
    var runRotateLeftAnimation = -1
    var runRotateRightAnimation = -1
    var crawlAnimation = -1
    var crawlRotate180Animation = -1
    var crawlRotateLeftAnimation = -1
    var crawlRotateRightAnimation = -1

    var options: Array<String?> = Array(5) { "" }

    var recolorSrc : Array<Int> = emptyArray<Int>()
    var recolorDest : Array<Int> = emptyArray<Int>()
    var retextureSrc : Array<Int> = emptyArray<Int>()
    var retextureDest : Array<Int> = emptyArray<Int>()

    var transforms: Array<Int>? = null
    var chatHeadModels: Array<Int>? = null
    val params = Int2ObjectOpenHashMap<Any>()
    var examine: String? = null
    var headIconGroups: Array<Int> = DEFAULT_HEAD_ICONS
    var headIconIndexes: Array<Int> = DEFAULT_HEAD_ICONS

    fun isAttackable(): Boolean = combatLevel > 0 && options.any { it == "Attack" }

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buf.readUnsignedByte().toInt()
                val models = IntArray(count)
                repeat(count) {
                    models[it] =buf.readUnsignedShort()
                }
                this.models = models.toTypedArray()
            }
            2 -> name = buf.readString()
            12 -> size = buf.readUnsignedByte().toInt()
            13 -> standAnim = buf.readUnsignedShort()
            14 -> walkAnim = buf.readUnsignedShort()
            15 -> rotateLeftAnim = buf.readUnsignedShort()
            16 -> rotateRightAnim = buf.readUnsignedShort()
            17 -> {
                walkAnim = buf.readUnsignedShort()
                rotateBackAnim = buf.readUnsignedShort()
                walkLeftAnim = buf.readUnsignedShort()
                walkRightAnim = buf.readUnsignedShort()
            }
            18 -> category = buf.readUnsignedShort()
            in 30 until 35 -> {
                options[opcode - 30] = buf.readString()
                if (options[opcode - 30].equals("null", true)
                    || options[opcode - 30].equals("hidden", true)) {
                    options[opcode - 30] = null
                }
            }
            40, 41 -> {
                val count = buf.readUnsignedByte().toInt()
                val src = IntArray(count)
                val dest = IntArray(count)
                repeat(count) {
                    src[it] = buf.readUnsignedShort()
                    dest[it] = buf.readUnsignedShort()
                }
                if (opcode == 40) {
                    recolorSrc = src.toTypedArray()
                    recolorDest = dest.toTypedArray()
                } else {
                    retextureSrc = src.toTypedArray()
                    retextureDest = dest.toTypedArray()
                }
            }
            60 -> {
                val count = buf.readUnsignedByte().toInt()
                val models = IntArray(count)
                repeat(count) {
                    models[it] = buf.readUnsignedShort()
                }
                chatHeadModels = models.toTypedArray()
            }
            93 -> isMinimapVisible = false
            95 -> combatLevel = buf.readUnsignedShort()
            97 -> widthScale = buf.readUnsignedShort()
            98 -> heightScale = buf.readUnsignedShort()
            99 -> renderPriority = true
            100 -> ambient = buf.readByte().toInt()
            101 -> contrast = buf.readByte() * 5
            102 -> {
                /** Yoinked from github.com/rsmod/rsmod :Kekw: */
                val initialBits = buf.readUnsignedByte().toInt()
                var bits = initialBits
                var count = 0
                while (bits != 0) {
                    count++
                    bits = bits shr 1
                }
                val groups = mutableListOf<Int>()
                val indexes = mutableListOf<Int>()
                repeat(count) { i ->
                    if ((initialBits and 0x1 shl i) == 0) {
                        groups += -1
                        indexes += -1
                        return@repeat
                    }
                    groups += buf.readIntSmart()
                    indexes += buf.readShortSmart()
                }
                headIconGroups = groups.toTypedArray()
                headIconIndexes = indexes.toTypedArray()
            }
            103 -> rotation = buf.readUnsignedShort()
            106, 118 -> {
                transformVarbit = buf.readUnsignedShort()
                if (transformVarbit == 65535) transformVarbit = -1

                transformVarp = buf.readUnsignedShort()
                if (transformVarp == 65535) transformVarp = -1

                if (opcode == 118) {
                    defaultTransform = buf.readUnsignedShort()
                    if (defaultTransform == 65535) defaultTransform = -1
                }

                val count = buf.readUnsignedByte().toInt()
                transforms = Array(count + 1) { 0 }
                for (i in 0..count) {
                    val transformId = buf.readUnsignedShort()
                    transforms!![i] = if (transformId == 65535) -1 else transformId
                }
            }
            107 -> interactable = false
            109 -> clickable = false
            114 -> runAnimation = buf.readUnsignedShort()
            115 -> {
                runAnimation = buf.readUnsignedShort()
                runRotate180Animation = buf.readUnsignedShort()
                runRotateLeftAnimation = buf.readUnsignedShort()
                runRotateRightAnimation = buf.readUnsignedShort()
            }
            116 -> crawlAnimation = buf.readUnsignedShort()
            117 -> {
                crawlAnimation = buf.readUnsignedShort()
                crawlRotate180Animation = buf.readUnsignedShort()
                crawlRotateLeftAnimation = buf.readUnsignedShort()
                crawlRotateRightAnimation = buf.readUnsignedShort()
            }
            122 -> lowPriorityFollowerOps = true
            123 -> isPet = true
            249 -> params.putAll(readParams(buf))
            else -> throw IllegalStateException("Unknown opcode: $opcode in NpcDef")
        }
    }
}

fun ByteBuf.readShortSmart(): Int {
    val peek = getUnsignedByte(readerIndex()).toInt()
    return if ((peek and 0x80) == 0) {
        readUnsignedByte().toInt() - 0x40
    } else {
        (readUnsignedShort() and 0x7FFF) - 0x4000
    }
}

fun ByteBuf.readIntSmart(): Int {
    val peek = getUnsignedByte(readerIndex()).toInt()
    return if ((peek and 0x80) == 0) {
        readUnsignedShort() - 0x4000
    } else {
        (readInt() and 0x7FFFFFFF) - 0x40000000
    }
}