package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.MoveMinimapClickMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MoveMinimapClickDecoder : MessageDecoder<MoveMinimapClickMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): MoveMinimapClickMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val type = values["movement_type"]!!.toInt()

        return MoveMinimapClickMessage(x, z, type)
    }
}