package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentItemEncoder : MessageEncoder<SetComponentItemMessage>() {

    override fun extract(message: SetComponentItemMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "item" -> message.item
        "amount" -> message.amount
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentItemMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}