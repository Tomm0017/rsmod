package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.TriggerOnDialogAbortMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OnDialogAbortEncoder : MessageEncoder<TriggerOnDialogAbortMessage>() {

    override fun extract(message: TriggerOnDialogAbortMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: TriggerOnDialogAbortMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}