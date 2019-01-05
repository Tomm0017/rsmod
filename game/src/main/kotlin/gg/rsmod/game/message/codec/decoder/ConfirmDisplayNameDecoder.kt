package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ConfirmDisplayNameMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ConfirmDisplayNameDecoder : MessageDecoder<ConfirmDisplayNameMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ConfirmDisplayNameMessage {
        val name = stringValues["name"]!!
        return ConfirmDisplayNameMessage(name)
    }
}