package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ChangeDisplayModeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DisplayModeDecoder : MessageDecoder<ChangeDisplayModeMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ChangeDisplayModeMessage {
        return ChangeDisplayModeMessage(values["mode"]!!.toInt(), values["width"]!!.toInt(), values["height"]!!.toInt())
    }

}