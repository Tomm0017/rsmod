package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpObjUMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObjUDecoder : MessageDecoder<OpObjUMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpObjUMessage {
        val component = values["component_hash"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val groundItem = values["ground_item"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        return OpObjUMessage(component, movementType, item, slot, groundItem, x, z)
    }
}