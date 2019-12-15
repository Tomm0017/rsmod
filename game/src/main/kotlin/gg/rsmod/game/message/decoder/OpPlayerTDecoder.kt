package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpPlayerTMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpPlayerTDecoder : MessageDecoder<OpPlayerTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayerTMessage {
        val componentHash = values["component_hash"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        val componentSlot = values["component_slot"]!!.toInt()
        val index = values["index"]!!.toInt()
        return OpPlayerTMessage(index, componentHash, componentSlot, movementType)
    }
}