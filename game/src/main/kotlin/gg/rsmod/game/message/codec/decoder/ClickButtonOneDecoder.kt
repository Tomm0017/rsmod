package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ClickButtonMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickButtonOneDecoder : MessageDecoder<ClickButtonMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickButtonMessage {
        val hash = values["hash"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val item = values["item"]!!.toInt()
        return ClickButtonMessage(hash = hash, option = opcodeIndex, slot = if (slot == 0xFFFF) -1 else slot, item = if (item == 0xFFFF) -1 else item)
    }
}