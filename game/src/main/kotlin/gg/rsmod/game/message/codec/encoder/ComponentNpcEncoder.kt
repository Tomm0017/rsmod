package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentNpcMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentNpcEncoder : MessageEncoder<SetComponentNpcMessage>() {

    override fun extract(message: SetComponentNpcMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "npc" -> message.npc
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentNpcMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}