package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.NpcAttackActionMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcAttackActionDecoder : MessageDecoder<NpcAttackActionMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): NpcAttackActionMessage {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return NpcAttackActionMessage(index, movement)
    }
}