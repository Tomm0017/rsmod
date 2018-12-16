package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetInterfaceAnimationMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceAnimationEncoder : MessageEncoder<SetInterfaceAnimationMessage>() {

    override fun extract(message: SetInterfaceAnimationMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "anim" -> message.anim
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetInterfaceAnimationMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}