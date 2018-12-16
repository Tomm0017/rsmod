package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetDisplayInterfaceMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DisplayInterfaceEncoder : MessageEncoder<SetDisplayInterfaceMessage>() {

    override fun extract(message: SetDisplayInterfaceMessage, key: String): Number = when (key) {
        "interfaceId" -> message.interfaceId
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetDisplayInterfaceMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}