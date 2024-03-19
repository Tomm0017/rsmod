package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IgnoreListAddMessage

class IgnoreListAddDecoder : MessageDecoder<IgnoreListAddMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): IgnoreListAddMessage {
        val name = stringValues["name"]!!.toString()
        return IgnoreListAddMessage(name)
    }
}