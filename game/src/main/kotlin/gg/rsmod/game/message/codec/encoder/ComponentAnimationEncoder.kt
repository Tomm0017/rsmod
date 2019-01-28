package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetComponentAnimationMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ComponentAnimationEncoder : MessageEncoder<SetComponentAnimationMessage>() {

    override fun extract(message: SetComponentAnimationMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        "anim" -> message.anim
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetComponentAnimationMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}