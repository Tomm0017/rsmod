package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.EventAppletFocusMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventAppletFocusDecoder : MessageDecoder<EventAppletFocusMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventAppletFocusMessage {
        val state = values["state"]!!.toInt()
        return EventAppletFocusMessage(state)
    }
}