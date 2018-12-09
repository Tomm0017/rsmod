package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SendChatboxTextMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChatboxTextEncoder(override val structure: MessageStructure) : MessageEncoder<SendChatboxTextMessage>(structure) {

    override fun extract(message: SendChatboxTextMessage, key: String): Number = when (key) {
        "type" -> message.type
        "check_ignore" -> if (message.username != null) 1 else 0
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SendChatboxTextMessage, key: String): ByteArray = when (key) {
        "username" -> {
            if (message.username != null) {
                val data = ByteArray(message.username.length + 1)
                System.arraycopy(message.username.toByteArray(), 0, data, 0, data.size - 1)
                data[data.size - 1] = 0
                data
            } else {
                ByteArray(0)
            }
        }
        "message" -> {
            val data = ByteArray(message.message.length + 1)
            System.arraycopy(message.message.toByteArray(), 0, data, 0, data.size - 1)
            data[data.size - 1] = 0
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}