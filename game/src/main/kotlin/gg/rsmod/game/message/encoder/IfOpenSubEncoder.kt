package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfOpenSubMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfOpenSubEncoder : MessageEncoder<IfOpenSubMessage>() {

    override fun extract(message: IfOpenSubMessage, key: String): Number = when (key) {
        "component" -> message.component
        "overlay" -> (message.parent shl 16) or message.child
        "type" -> message.type
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfOpenSubMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}