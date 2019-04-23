package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.MapBuildCompleteMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapBuildCompleteDecoder : MessageDecoder<MapBuildCompleteMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): MapBuildCompleteMessage = MapBuildCompleteMessage()
}