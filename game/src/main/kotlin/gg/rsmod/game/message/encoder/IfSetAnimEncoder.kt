package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfSetAnimMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetAnimEncoder : MessageEncoder<IfSetAnimMessage>() {

    override fun extract(message: IfSetAnimMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "anim" -> message.anim
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetAnimMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}