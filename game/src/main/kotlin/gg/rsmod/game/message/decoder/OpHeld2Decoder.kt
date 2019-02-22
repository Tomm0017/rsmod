package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpHeld2Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld2Decoder : MessageDecoder<OpHeld2Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpHeld2Message {
        val hash = values["component_hash"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()

        return OpHeld2Message(if (item == 0xFFFF) -1 else item, if (slot == 0xFFFF) -1 else slot, hash)
    }
}