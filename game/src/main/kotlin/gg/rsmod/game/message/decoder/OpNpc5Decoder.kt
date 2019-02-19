package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpNpc5Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc5Decoder : MessageDecoder<OpNpc5Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc5Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc5Message(index, movement)
    }
}