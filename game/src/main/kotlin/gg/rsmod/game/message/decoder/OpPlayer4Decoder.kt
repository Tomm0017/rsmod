package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpPlayer4Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer4Decoder : MessageDecoder<OpPlayer4Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer4Message {
        val index = values["index"]!!.toInt()
        return OpPlayer4Message(index)
    }
}