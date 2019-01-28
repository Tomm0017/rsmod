package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.OpenComponentMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpenComponentEncoder : MessageEncoder<OpenComponentMessage>() {

    override fun extract(message: OpenComponentMessage, key: String): Number = when (key) {
        "component" -> message.component
        "overlay" -> (message.parent shl 16) or message.child
        "type" -> message.type
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: OpenComponentMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}