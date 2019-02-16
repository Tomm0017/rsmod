package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.UpdateZonePartialFollowsMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateZonePartialFollowsEncoder : MessageEncoder<UpdateZonePartialFollowsMessage>() {

    override fun extract(message: UpdateZonePartialFollowsMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateZonePartialFollowsMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}