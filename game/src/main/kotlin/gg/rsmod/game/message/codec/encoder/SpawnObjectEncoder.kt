package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SpawnObjectMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnObjectEncoder : MessageEncoder<SpawnObjectMessage>() {

    override fun extract(message: SpawnObjectMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        "id" -> message.id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SpawnObjectMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}