package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceNpcMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceNpcEncoder(override val structure: MessageStructure) : MessageEncoder<SetInterfaceNpcMessage>(structure) {

    override fun extract(message: SetInterfaceNpcMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "npc" -> message.npc
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceNpcMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}