package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.CameraShakeMessage

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class CameraShakeEncoder : MessageEncoder<CameraShakeMessage>() {

    override fun extract(message: CameraShakeMessage, key: String): Number = when (key) {
        "cam_index" -> message.cam_index
        "sinus_x" -> message.sinus_x
        "amplitude" -> message.amplitude
        "sinus_y" -> message.sinus_y
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: CameraShakeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}