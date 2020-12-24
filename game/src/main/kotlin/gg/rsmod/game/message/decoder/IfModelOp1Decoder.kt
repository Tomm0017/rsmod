package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IfModelOp1Message

class IfModelOp1Decoder : MessageDecoder<IfModelOp1Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IfModelOp1Message {
        val component = values["component"]!!.toInt()
        return IfModelOp1Message(component = component)
    }
}