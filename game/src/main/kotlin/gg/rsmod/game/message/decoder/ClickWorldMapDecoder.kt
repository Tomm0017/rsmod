package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.ClickWorldMapMessage

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapDecoder : MessageDecoder<ClickWorldMapMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickWorldMapMessage {
        val data = values["data"]!!.toInt()
        return ClickWorldMapMessage(data)
    }
}