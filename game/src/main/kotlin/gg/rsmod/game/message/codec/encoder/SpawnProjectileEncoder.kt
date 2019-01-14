package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SpawnProjectileMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnProjectileEncoder : MessageEncoder<SpawnProjectileMessage>() {

    override fun extract(message: SpawnProjectileMessage, key: String): Number = when (key) {
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

    override fun extractBytes(message: SpawnProjectileMessage, key: String): ByteArray = throw Exception("Unhandled value key.")

}