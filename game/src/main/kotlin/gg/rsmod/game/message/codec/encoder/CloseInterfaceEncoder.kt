package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.CloseInterfaceMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseInterfaceEncoder : MessageEncoder<CloseInterfaceMessage>() {

    override fun extract(message: CloseInterfaceMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: CloseInterfaceMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}