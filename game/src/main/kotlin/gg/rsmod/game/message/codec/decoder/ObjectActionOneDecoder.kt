package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ObjectActionOneMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectActionOneDecoder : MessageDecoder<ObjectActionOneMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ObjectActionOneMessage {
        val id = values["id"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        return ObjectActionOneMessage(id, x, z, movementType)
    }

}