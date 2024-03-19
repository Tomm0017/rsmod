package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpObjTMessage

class OpObjTDecoder : MessageDecoder<OpObjTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpObjTMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val obj = values["obj"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        val hash = values["hash"]!!.toInt()
        return OpObjTMessage(x = x, z = z, slot = slot, item = item, obj = obj, movementType = movementType, hash = hash)
    }
}