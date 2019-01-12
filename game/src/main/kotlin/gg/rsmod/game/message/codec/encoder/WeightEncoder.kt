package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.WeightMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WeightEncoder : MessageEncoder<WeightMessage>() {

    override fun extract(message: WeightMessage, key: String): Number = when (key) {
        "weight" -> message.weight
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: WeightMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}