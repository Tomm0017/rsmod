package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpNpcTMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcTDecoder : MessageDecoder<OpNpcTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpcTMessage {
        val npcIndex = values["npc_index"]!!.toInt()
        val componentHash = values["component_hash"]!!.toInt()
        val componentSlot = values["component_slot"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return OpNpcTMessage(npcIndex, componentHash, componentSlot, movementType)
    }
}