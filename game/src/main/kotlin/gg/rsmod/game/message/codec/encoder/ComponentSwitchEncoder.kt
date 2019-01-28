package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.ComponentSwitchMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentSwitchEncoder : MessageEncoder<ComponentSwitchMessage>() {

    override fun extract(message: ComponentSwitchMessage, key: String): Number = when (key) {
        "from" -> message.from
        "to" -> message.to
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: ComponentSwitchMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}