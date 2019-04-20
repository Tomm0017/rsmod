package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpPlayer8Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer8Decoder : MessageDecoder<OpPlayer8Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer8Message {
        val index = values["index"]!!.toInt()
        return OpPlayer8Message(index)
    }
}