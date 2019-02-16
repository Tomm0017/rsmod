package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.SoundAreaMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SoundAreaEncoder : MessageEncoder<SoundAreaMessage>() {

    override fun extract(message: SoundAreaMessage, key: String): Number = when (key) {
        "sound" -> message.id
        "tile" -> message.tileHash
        "settings" -> ((message.radius and 0xf) shl 4) or (message.volume and 0x7)
        "delay" -> message.delay
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SoundAreaMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}