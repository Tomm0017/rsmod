package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class MessagePublicMessage(val type: Int, val color: Int, val effect: Int, val length: Int, val data: ByteArray) : Message {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessagePublicMessage

        if (type != other.type) return false
        if (color != other.color) return false
        if (effect != other.effect) return false
        if (length != other.length) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + color
        result = 31 * result + effect
        result = 31 * result + length
        result = 31 * result + data.contentHashCode()
        return result
    }
}