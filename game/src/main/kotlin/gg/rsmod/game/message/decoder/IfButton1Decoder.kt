package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IfButtonMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Decoder : MessageDecoder<IfButtonMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IfButtonMessage {
        val hash = values["hash"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val item = values["item"]!!.toInt()
        return IfButtonMessage(hash = hash, option = opcodeIndex, slot = if (slot == 0xFFFF) -1 else slot, item = if (item == 0xFFFF) -1 else item)
    }
}