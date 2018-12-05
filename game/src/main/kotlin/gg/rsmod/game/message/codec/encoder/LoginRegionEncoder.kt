package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.LoginRegionMessage

/**
 * Responsible for extracting values from the [LoginRegionMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginRegionEncoder(override val structure: MessageStructure) : MessageEncoder<LoginRegionMessage>(structure) {

    override fun extract(message: LoginRegionMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        "regions" -> message.regions
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LoginRegionMessage, key: String): ByteArray = when (key) {
        "xteas" -> message.xteas
        "gpi" -> message.gpi
        else -> throw Exception("Unhandled value key.")
    }
}