package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpHeld1Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld1Decoder : MessageDecoder<OpHeld1Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpHeld1Message {
        val hash = values["component_hash"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        return OpHeld1Message(if (item == 0xFFFF) -1 else item, if (slot == 0xFFFF) -1 else slot, hash)
    }
}