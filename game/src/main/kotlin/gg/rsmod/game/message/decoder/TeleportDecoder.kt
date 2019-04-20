package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.TeleportMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TeleportDecoder : MessageDecoder<TeleportMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): TeleportMessage {
        val unknown = values["unknown"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val height = values["height"]!!.toInt()
        return TeleportMessage(unknown, x, z, height)
    }
}