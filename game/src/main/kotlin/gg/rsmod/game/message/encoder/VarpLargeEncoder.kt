package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.VarpLargeMessage
import gg.rsmod.game.message.impl.VarpSmallMessage

/**
 * Responsible for extracting values from the [VarpSmallMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class VarpLargeEncoder : MessageEncoder<VarpLargeMessage>() {

    override fun extract(message: VarpLargeMessage, key: String): Number = when (key) {
        "id" -> message.id
        "value" -> message.value
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: VarpLargeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}