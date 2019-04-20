package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.MapProjAnimMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapProjAnimEncoder : MessageEncoder<MapProjAnimMessage>() {

    override fun extract(message: MapProjAnimMessage, key: String): Number = when (key) {
        "end_height" -> message.endHeight
        "lifespan" -> message.lifespan
        "gfx" -> message.gfx
        "start_height" -> message.startHeight
        "target_index" -> message.pawnTargetIndex
        "angle" -> message.angle
        "steepness" -> message.steepness
        "delay" -> message.delay
        "tile" -> message.start
        "offset_x" -> message.offsetX
        "offset_z" -> message.offsetZ
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: MapProjAnimMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}