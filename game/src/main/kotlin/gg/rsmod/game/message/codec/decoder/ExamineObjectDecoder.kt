package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ExamineObjectMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ExamineObjectDecoder : MessageDecoder<ExamineObjectMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ExamineObjectMessage {
        val id = values["id"]!!.toInt()
        return ExamineObjectMessage(id)
    }

}