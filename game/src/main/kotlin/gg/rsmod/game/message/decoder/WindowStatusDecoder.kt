package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.WindowStatusMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WindowStatusDecoder : MessageDecoder<WindowStatusMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): WindowStatusMessage {
        return WindowStatusMessage(values["mode"]!!.toInt(), values["width"]!!.toInt(), values["height"]!!.toInt())
    }
}