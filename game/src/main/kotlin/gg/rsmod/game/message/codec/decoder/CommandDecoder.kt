package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.CommandMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CommandDecoder : MessageDecoder<CommandMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): CommandMessage {
        return CommandMessage(stringValues["command"]!!)
    }
}
