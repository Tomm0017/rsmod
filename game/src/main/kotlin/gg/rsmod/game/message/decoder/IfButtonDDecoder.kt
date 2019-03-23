package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IfButtonDMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButtonDDecoder : MessageDecoder<IfButtonDMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IfButtonDMessage {
        val srcComponentHash = values["src_component_hash"]!!.toInt()
        val srcSlot = values["src_slot"]!!.toInt()
        val srcItem = values["src_item"]!!.toInt()
        val dstComponentHash = values["dst_component_hash"]!!.toInt()
        val dstSlot = values["dst_slot"]!!.toInt()
        val dstItem = values["dst_item"]!!.toInt()

        return IfButtonDMessage(srcComponentHash, srcSlot, srcItem, dstComponentHash, dstSlot, dstItem)
    }
}