package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpNpc4Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc4Decoder : MessageDecoder<OpNpc4Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc4Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc4Message(index, movement)
    }
}