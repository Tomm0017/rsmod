package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpPlayer5Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer5Decoder : MessageDecoder<OpPlayer5Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer5Message {
        val index = values["index"]!!.toInt()
        return OpPlayer5Message(index)
    }
}