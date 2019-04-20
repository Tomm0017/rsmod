package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpLoc4Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc4Decoder : MessageDecoder<OpLoc4Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpLoc4Message {
        val id = values["id"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return OpLoc4Message(id, x, z, movementType)
    }
}