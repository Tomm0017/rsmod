package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetChunkToRegionOffset

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapChunkEncoder : MessageEncoder<SetChunkToRegionOffset>() {

    override fun extract(message: SetChunkToRegionOffset, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetChunkToRegionOffset, key: String): ByteArray = throw Exception("Unhandled value key.")
}