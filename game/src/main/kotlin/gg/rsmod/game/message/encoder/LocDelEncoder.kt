package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.LocDelMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LocDelEncoder : MessageEncoder<LocDelMessage>() {

    override fun extract(message: LocDelMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LocDelMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}