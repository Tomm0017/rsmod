package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.CloseMainComponentMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainComponentDecoder : MessageDecoder<CloseMainComponentMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): CloseMainComponentMessage {
        return CloseMainComponentMessage()
    }
}