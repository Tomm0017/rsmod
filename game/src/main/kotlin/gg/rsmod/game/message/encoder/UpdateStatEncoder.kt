package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.UpdateStatMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateStatEncoder : MessageEncoder<UpdateStatMessage>() {

    override fun extract(message: UpdateStatMessage, key: String): Number = when (key) {
        "level" -> message.level
        "xp" -> message.xp
        "skill" -> message.skill
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateStatMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}