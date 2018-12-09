package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceSettingMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceSettingEncoder(override val structure: MessageStructure) : MessageEncoder<SetInterfaceSettingMessage>(structure) {

    override fun extract(message: SetInterfaceSettingMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "from" -> message.fromChild
        "to" -> message.toChild
        "setting" -> message.setting
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceSettingMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}