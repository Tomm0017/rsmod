package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceTextMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceTextEncoder(override val structure: MessageStructure) : MessageEncoder<SetInterfaceTextMessage>(structure) {

    override fun extract(message: SetInterfaceTextMessage, key: String): Number = when (key) {
        "hash" -> (message.parent shl 16) or message.child
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceTextMessage, key: String): ByteArray = when (key) {
        "text" -> {
            val data = ByteArray(message.text.length + 1)
            System.arraycopy(message.text.toByteArray(), 0, data, 0, data.size - 1)
            data[data.size - 1] = 0
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}