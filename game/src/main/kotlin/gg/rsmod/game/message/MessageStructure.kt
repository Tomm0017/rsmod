package gg.rsmod.game.message

import gg.rsmod.net.packet.PacketType
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap

/**
 * The structure of a [Message].
 *
 * @param type
 * The [PacketType].
 *
 * @param opcodes
 * An array of opcodes that can be used to identify this structure.
 *
 * @param length
 * The length of the payload, if any.
 *
 * @param ignore
 * If the packet should be skipped over and not processed by the server.
 *
 * @param values
 * The [MessageValue]s. The order is important as it will be
 * encoded and decoded based on its order. This is why it's using the
 * [LinkedHashMap] implementation where insert-order is maintained.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class MessageStructure(val type: PacketType, val opcodes: IntArray, val length: Int,
                            val ignore: Boolean, val values: Object2ObjectLinkedOpenHashMap<String, MessageValue>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageStructure

        if (type != other.type) return false
        if (!opcodes.contentEquals(other.opcodes)) return false
        if (length != other.length) return false
        if (ignore != other.ignore) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + opcodes.contentHashCode()
        result = 31 * result + length
        result = 31 * result + ignore.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }

}