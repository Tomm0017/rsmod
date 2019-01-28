package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentSettingMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentSettingEncoder : MessageEncoder<SetComponentSettingMessage>() {

    override fun extract(message: SetComponentSettingMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "from" -> message.fromChild
        "to" -> message.toChild
        "setting" -> message.setting
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentSettingMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}