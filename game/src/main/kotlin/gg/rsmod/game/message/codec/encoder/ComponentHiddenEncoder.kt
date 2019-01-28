package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentHiddenMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentHiddenEncoder : MessageEncoder<SetComponentHiddenMessage>() {

    override fun extract(message: SetComponentHiddenMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "hidden" -> if (message.hidden) 1 else 0
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentHiddenMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}