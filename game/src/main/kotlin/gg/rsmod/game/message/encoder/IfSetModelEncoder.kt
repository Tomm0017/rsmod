package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfSetModelMessage

class IfSetModelEncoder : MessageEncoder<IfSetModelMessage>() {

    override fun extract(message: IfSetModelMessage, key: String): Number =
        when (key) {
        "hash" -> message.hash
        "model_id" -> message.model_id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetModelMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}