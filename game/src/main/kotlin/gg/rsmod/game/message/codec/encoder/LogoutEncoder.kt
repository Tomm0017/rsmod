package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SendLogoutMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LogoutEncoder(override val structure: MessageStructure) : MessageEncoder<SendLogoutMessage>(structure) {

    override fun extract(message: SendLogoutMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: SendLogoutMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}