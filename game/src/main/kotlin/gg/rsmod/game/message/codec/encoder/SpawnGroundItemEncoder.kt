package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SpawnGroundItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnGroundItemEncoder : MessageEncoder<SpawnGroundItemMessage>() {

    override fun extract(message: SpawnGroundItemMessage, key: String): Number = when (key) {
        "item" -> message.item
        "amount" -> message.amount
        "tile" -> message.tile
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SpawnGroundItemMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}