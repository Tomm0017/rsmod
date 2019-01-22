package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.SpellOnNpcMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpellOnNpcDecoder : MessageDecoder<SpellOnNpcMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): SpellOnNpcMessage {
        val npcIndex = values["npc_index"]!!.toInt()
        val interfaceHash = values["interface_hash"]!!.toInt()
        val interfaceSlot = values["interface_slot"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return SpellOnNpcMessage(npcIndex, interfaceHash, interfaceSlot, movementType)
    }
}