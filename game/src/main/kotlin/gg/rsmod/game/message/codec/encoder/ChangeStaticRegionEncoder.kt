package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.ChangeStaticRegionMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChangeStaticRegionEncoder(override val structure: MessageStructure) : MessageEncoder<ChangeStaticRegionMessage>(structure) {

    override fun extract(message: ChangeStaticRegionMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        "regions" -> message.regions
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: ChangeStaticRegionMessage, key: String): ByteArray = when (key) {
        "xteas" -> message.xteas
        else -> throw Exception("Unhandled value key.")
    }
}