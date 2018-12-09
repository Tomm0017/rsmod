package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SendSkillMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SendSkillEncoder(override val structure: MessageStructure) : MessageEncoder<SendSkillMessage>(structure) {

    override fun extract(message: SendSkillMessage, key: String): Number = when (key) {
        "level" -> message.level
        "xp" -> message.xp
        "skill" -> message.skill
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SendSkillMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}