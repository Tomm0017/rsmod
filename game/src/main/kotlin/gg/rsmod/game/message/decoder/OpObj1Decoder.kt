package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpObj1Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObj1Decoder : MessageDecoder<OpObj1Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpObj1Message {
        val item = values["item"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return OpObj1Message(item, x, z, movementType)
    }
}