package gg.rsmod.game.message.codec.decoder

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.impl.ClickMapMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapDecoder : MessageDecoder<ClickMapMessage>() {

    override fun decode(values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickMapMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val type = values["movement_type"]!!.toInt()

        return ClickMapMessage(x, z, type)
    }

}