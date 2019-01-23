package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.PlayAreaSoundMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayAreaSoundEncoder : MessageEncoder<PlayAreaSoundMessage>() {

    override fun extract(message: PlayAreaSoundMessage, key: String): Number = when (key) {
        "sound" -> message.id
        "tile" -> message.tileHash
        "settings" -> ((message.radius and 0xf) shl 4) or (message.volume and 0x7)
        "delay" -> message.delay
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: PlayAreaSoundMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}