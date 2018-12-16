package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.RemoveObjectMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RemoveObjectEncoder : MessageEncoder<RemoveObjectMessage>() {

    override fun extract(message: RemoveObjectMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: RemoveObjectMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}