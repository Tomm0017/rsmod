package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.IfModelOpMessage

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class IfModelOpDecoder : MessageDecoder<IfModelOpMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): IfModelOpMessage {
        val component = values["component"]!!.toInt()
        return IfModelOpMessage(component = component)
    }
}