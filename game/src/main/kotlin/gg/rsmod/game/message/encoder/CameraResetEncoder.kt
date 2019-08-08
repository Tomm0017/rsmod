package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.CameraResetMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CameraResetEncoder : MessageEncoder<CameraResetMessage>() {

    override fun extract(message: CameraResetMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: CameraResetMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}