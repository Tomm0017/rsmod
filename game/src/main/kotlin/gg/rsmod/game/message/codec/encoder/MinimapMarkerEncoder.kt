package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetMinimapMarkerMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MinimapMarkerEncoder : MessageEncoder<SetMinimapMarkerMessage>() {

    override fun extract(message: SetMinimapMarkerMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetMinimapMarkerMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}