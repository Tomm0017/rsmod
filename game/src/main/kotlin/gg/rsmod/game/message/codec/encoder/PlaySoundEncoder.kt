package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.PlaySoundMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlaySoundEncoder : MessageEncoder<PlaySoundMessage>() {

    override fun extract(message: PlaySoundMessage, key: String): Number = when (key) {
        "sound" -> message.sound
        "volume" -> message.volume
        "delay" -> message.delay
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: PlaySoundMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}