package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.MidiJingleMessage

/**
 * @author bmyte
 *
 * Note| the client reads but does not use the garbage_delay key implemented
 *   but could be workings of eventual/previous ability for delayed jingles??
 */
class MidiJingleEncoder : MessageEncoder<MidiJingleMessage>() {

    override fun extract(message: MidiJingleMessage, key: String): Number = when (key) {
        "id" -> message.id
        "garbage_delay" -> 0
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: MidiJingleMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}