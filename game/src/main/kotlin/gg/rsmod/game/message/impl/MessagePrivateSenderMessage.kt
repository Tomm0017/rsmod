package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

data class MessagePrivateSenderMessage(val target: String, val length: Int, val message: ByteArray) : Message {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessagePrivateSenderMessage

        if (target != other.target) return false
        if (length != other.length) return false
        if (!message.contentEquals(other.message)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = target.hashCode()
        result = 31 * result + length
        result = 31 * result + message.contentHashCode()
        return result
    }
}