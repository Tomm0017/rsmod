package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpLoc6Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Decoder : MessageDecoder<OpLoc6Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpLoc6Message {
        val id = values["id"]!!.toInt()
        return OpLoc6Message(id)
    }
}