package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetMapChunkMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapChunkEncoder(override val structure: MessageStructure) : MessageEncoder<SetMapChunkMessage>(structure) {

    override fun extract(message: SetMapChunkMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetMapChunkMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}