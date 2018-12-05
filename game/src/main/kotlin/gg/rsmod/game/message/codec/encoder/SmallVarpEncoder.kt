package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.SmallVarpMessage

/**
 * Responsible for extracting values from the [SmallVarpMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SmallVarpEncoder(override val structure: MessageStructure) : MessageEncoder<SmallVarpMessage>(structure) {

    override fun extract(message: SmallVarpMessage, key: String): Number = when (key) {
        "id" -> message.id
        "value" -> message.value
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SmallVarpMessage, key: String): ByteArray = throw Exception("Unhandled value key.")

}