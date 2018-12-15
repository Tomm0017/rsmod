package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SpawnObjectMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnObjectEncoder(override val structure: MessageStructure) : MessageEncoder<SpawnObjectMessage>(structure) {

    override fun extract(message: SpawnObjectMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        "id" -> message.id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SpawnObjectMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}