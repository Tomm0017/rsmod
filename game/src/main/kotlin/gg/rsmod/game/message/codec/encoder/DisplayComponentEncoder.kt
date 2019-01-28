package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetDisplayComponentMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DisplayComponentEncoder : MessageEncoder<SetDisplayComponentMessage>() {

    override fun extract(message: SetDisplayComponentMessage, key: String): Number = when (key) {
        "component" -> message.component
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetDisplayComponentMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}