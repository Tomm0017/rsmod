package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.UpdateGroundItemMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateGroundItemEncoder : MessageEncoder<UpdateGroundItemMessage>() {

    override fun extract(message: UpdateGroundItemMessage, key: String): Number = when (key) {
        "item" -> message.item
        "old_amount" -> message.oldAmount
        "new_amount" -> message.newAmount
        "tile" -> message.tile
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateGroundItemMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}