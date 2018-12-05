package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ClickMapMovementMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapDecoder : MessageDecoder<ClickMapMovementMessage>() {

    override fun decode(values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickMapMovementMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val type = values["movement_type"]!!.toInt()

        return ClickMapMovementMessage(x, z, type)
    }

}