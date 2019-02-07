package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ResumePCountDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePCountDialogDecoder : MessageDecoder<ResumePCountDialogMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ResumePCountDialogMessage {
        val input = values["input"]!!.toInt()
        return ResumePCountDialogMessage(input)
    }
}