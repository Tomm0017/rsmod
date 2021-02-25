package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpPlayerTMessage

class OpPlayerTDecoder : MessageDecoder<OpPlayerTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayerTMessage {
        val playerIndex = values["player_index"]!!.toInt()
        val keydown = values["keydown"]!!.toInt() == 1
        val spellChildIndex = values["spell_child_index"]!!.toInt()
        val componentHash = values["component_hash"]!!.toInt()
        return OpPlayerTMessage(playerIndex, keydown, spellChildIndex, componentHash)
    }
}