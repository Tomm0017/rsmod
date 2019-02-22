package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.UpdateAppearanceMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceDecoder : MessageDecoder<UpdateAppearanceMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): UpdateAppearanceMessage {
        val appearance = IntArray(7)
        val colors = IntArray(5)

        val gender = values["gender"]!!.toInt()
        for (i in 0 until appearance.size) {
            appearance[i] = values["appearance$i"]!!.toInt()
        }
        for (i in 0 until colors.size) {
            colors[i] = values["color$i"]!!.toInt()
        }

        return UpdateAppearanceMessage(gender, appearance, colors)
    }
}