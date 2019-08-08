package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.CameraLookAtMessage
import gg.rsmod.game.message.impl.CameraMoveToMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CamMoveToEncoder : MessageEncoder<CameraMoveToMessage>() {

    override fun extract(message: CameraMoveToMessage, key: String): Number = when (key) {
        "scene_x" -> message.scene_x
        "scene_z" -> message.scene_z
        "height" -> message.height
        "param4" -> message.param4
        "param5" -> message.param5
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: CameraMoveToMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}