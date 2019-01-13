package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.GroundItemActionThreeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class GroundItemActionThreeDecoder : MessageDecoder<GroundItemActionThreeMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): GroundItemActionThreeMessage {
        val item = values["item"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()

        return GroundItemActionThreeMessage(item, x, z, movementType)
    }

}