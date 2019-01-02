package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ClickMinimapMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMinimapDecoder : MessageDecoder<ClickMinimapMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickMinimapMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val type = values["movement_type"]!!.toInt()

        return ClickMinimapMessage(x, z, type)
    }

}