package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.EventMouseClickMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventMouseClickDecoder : MessageDecoder<EventMouseClickMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventMouseClickMessage {
        val x = values["x"]!!.toInt()
        val y = values["y"]!!.toInt()

        return EventMouseClickMessage(x, y)
    }
}