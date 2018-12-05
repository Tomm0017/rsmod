package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.OpenInterfaceMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpenInterfaceEncoder(override val structure: MessageStructure) : MessageEncoder<OpenInterfaceMessage>(structure) {

    override fun extract(message: OpenInterfaceMessage, key: String): Number = when (key) {
        "interfaceId" -> message.interfaceId
        "overlay" -> (message.parent shl 16) or message.child
        "type" -> message.type
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: OpenInterfaceMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}