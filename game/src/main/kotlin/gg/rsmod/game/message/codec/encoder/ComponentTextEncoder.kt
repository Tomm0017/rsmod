package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentTextMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentTextEncoder : MessageEncoder<SetComponentTextMessage>() {

    override fun extract(message: SetComponentTextMessage, key: String): Number = when (key) {
        "hash" -> (message.parent shl 16) or message.child
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentTextMessage, key: String): ByteArray = when (key) {
        "text" -> {
            val data = ByteArray(message.text.length + 1)
            System.arraycopy(message.text.toByteArray(), 0, data, 0, data.size - 1)
            data[data.size - 1] = 0
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}