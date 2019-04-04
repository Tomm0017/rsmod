package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.UpdateRebootTimerMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateRebootTimerEncoder : MessageEncoder<UpdateRebootTimerMessage>() {

    override fun extract(message: UpdateRebootTimerMessage, key: String): Number = when (key) {
        "cycles" -> message.cycles
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateRebootTimerMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}