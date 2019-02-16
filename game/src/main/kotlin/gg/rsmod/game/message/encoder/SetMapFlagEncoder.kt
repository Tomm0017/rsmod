package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.SetMapFlagMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SetMapFlagEncoder : MessageEncoder<SetMapFlagMessage>() {

    override fun extract(message: SetMapFlagMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetMapFlagMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}