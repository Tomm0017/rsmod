package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.ResumePNameDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePNameDialogDecoder : MessageDecoder<ResumePNameDialogMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ResumePNameDialogMessage {
        val name = stringValues["name"]!!
        return ResumePNameDialogMessage(name)
    }
}