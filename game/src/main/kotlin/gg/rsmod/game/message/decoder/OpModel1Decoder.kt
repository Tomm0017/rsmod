package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpModel1Message

/**
 * Decodes [OpModel1Message]
 * TODO: Find out more about OpModel1Decoder (op 69, nice)
 *
 * @author Curtis Woodard <nbness1337@gmail.com>
 */
class OpModel1Decoder: MessageDecoder<OpModel1Message>() {
    override fun decode(
            opcode: Int,
            opcodeIndex: Int,
            values: HashMap<String, Number>,
            stringValues: HashMap<String, String>
    ): OpModel1Message =
            OpModel1Message(componentId = values["componentId"]!!.toInt())
}