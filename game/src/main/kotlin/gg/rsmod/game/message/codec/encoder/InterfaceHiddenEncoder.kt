package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceHiddenMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceHiddenEncoder : MessageEncoder<SetInterfaceHiddenMessage>() {

    override fun extract(message: SetInterfaceHiddenMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "hidden" -> if (message.hidden) 1 else 0
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceHiddenMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}