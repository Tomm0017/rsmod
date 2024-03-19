package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IfButtonTMessage

class IfButtonTDecoder : MessageDecoder<IfButtonTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IfButtonTMessage {
        val srcComponentHash = values["from_component_hash"]!!.toInt()
        val dstComponentHash = values["to_component_hash"]!!.toInt()
        val dstSlot = values["toSlot"]!!.toInt()
        val dstItem = values["toItem"]!!.toInt()
        val srcSlot = values["fromSlot"]!!.toInt()
        val srcItem = values["fromItem"]!!.toInt()

        return IfButtonTMessage(srcComponentHash, srcSlot, srcItem, dstComponentHash, dstSlot, dstItem)
    }
}