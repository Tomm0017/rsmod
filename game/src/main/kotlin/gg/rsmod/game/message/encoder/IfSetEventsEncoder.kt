package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfSetEventsMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetEventsEncoder : MessageEncoder<IfSetEventsMessage>() {

    override fun extract(message: IfSetEventsMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "from" -> message.fromChild
        "to" -> message.toChild
        "setting" -> message.setting
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetEventsMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}