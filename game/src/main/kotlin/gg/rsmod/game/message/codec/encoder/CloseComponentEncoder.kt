package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.CloseComponentMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseComponentEncoder : MessageEncoder<CloseComponentMessage>() {

    override fun extract(message: CloseComponentMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: CloseComponentMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}