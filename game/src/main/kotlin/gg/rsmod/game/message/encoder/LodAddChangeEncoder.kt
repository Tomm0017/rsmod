package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.LocAddChangeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LodAddChangeEncoder : MessageEncoder<LocAddChangeMessage>() {

    override fun extract(message: LocAddChangeMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        "id" -> message.id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LocAddChangeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}