package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.RemoveGroundItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RemoveGroundItemEncoder : MessageEncoder<RemoveGroundItemMessage>() {

    override fun extract(message: RemoveGroundItemMessage, key: String): Number = when (key) {
        "item" -> message.item
        "tile" -> message.tile
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: RemoveGroundItemMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}