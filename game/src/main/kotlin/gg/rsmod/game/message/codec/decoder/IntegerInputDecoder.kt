package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.IntegerInputMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IntegerInputDecoder : MessageDecoder<IntegerInputMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IntegerInputMessage {
        val input = values["input"]!!.toInt()
        return IntegerInputMessage(input)
    }
}