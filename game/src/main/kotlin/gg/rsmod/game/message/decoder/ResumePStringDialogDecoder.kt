package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.ResumePStringDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePStringDialogDecoder : MessageDecoder<ResumePStringDialogMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ResumePStringDialogMessage {
        val input = stringValues["input"]!!
        return ResumePStringDialogMessage(input)
    }
}