package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.InterfaceSwitchMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceSwitchEncoder : MessageEncoder<InterfaceSwitchMessage>() {

    override fun extract(message: InterfaceSwitchMessage, key: String): Number = when (key) {
        "from" -> message.from
        "to" -> message.to
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: InterfaceSwitchMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}