package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpObj4Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObj4Decoder : MessageDecoder<OpObj4Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpObj4Message {
        val item = values["item"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return OpObj4Message(item, x, z, movementType)
    }
}