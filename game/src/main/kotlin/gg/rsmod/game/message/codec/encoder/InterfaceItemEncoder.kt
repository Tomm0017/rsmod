package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceItemEncoder : MessageEncoder<SetInterfaceItemMessage>() {

    override fun extract(message: SetInterfaceItemMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "item" -> message.item
        "amount" -> message.amount
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceItemMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}