package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpNpcUMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcUDecoder : MessageDecoder<OpNpcUMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpcUMessage {
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val componentHash = values["component_hash"]!!.toInt()
        val npcIndex = values["npc_index"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return OpNpcUMessage(componentHash, npcIndex, item, slot, movementType)
    }
}