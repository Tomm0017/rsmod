package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.PlaySongMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlaySongEncoder : MessageEncoder<PlaySongMessage>() {

    override fun extract(message: PlaySongMessage, key: String): Number = when (key) {
        "id" -> message.id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: PlaySongMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}