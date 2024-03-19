package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.ObjAddMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjAddEncoder : MessageEncoder<ObjAddMessage>() {

    override fun extract(message: ObjAddMessage, key: String): Number = when (key) {
        "item" -> message.item
        "amount" -> message.amount
        "tile" -> message.tile
        "unknown1" -> 31
        "random1" -> 0  // Will change in next rev as of [211]
        "random2" -> 0  // Will change in next rev as of [211]
        "random3" -> 0  // Will change in next rev as of [211]
        "random4" -> 0  // Will change in next rev as of [211]
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: ObjAddMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}