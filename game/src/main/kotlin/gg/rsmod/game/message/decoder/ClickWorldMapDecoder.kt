package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.WorldMapClickMessage

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapDecoder : MessageDecoder<WorldMapClickMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): WorldMapClickMessage {
        val data = values["data"]!!.toInt()
        return WorldMapClickMessage(data)
    }
}