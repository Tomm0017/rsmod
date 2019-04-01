package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.RebuildNormalMessage
import gg.rsmod.game.model.region.Chunk
import io.netty.buffer.Unpooled

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RebuildNormalEncoder : MessageEncoder<RebuildNormalMessage>() {

    override fun extract(message: RebuildNormalMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: RebuildNormalMessage, key: String): ByteArray = when (key) {
        "xteas" -> {
            val chunkX = message.x
            val chunkZ = message.z

            val lx = (chunkX - (Chunk.MAX_VIEWPORT shr 4)) shr 3
            val rx = (chunkX + (Chunk.MAX_VIEWPORT shr 4)) shr 3
            val lz = (chunkZ - (Chunk.MAX_VIEWPORT shr 4)) shr 3
            val rz = (chunkZ + (Chunk.MAX_VIEWPORT shr 4)) shr 3

            val buf = Unpooled.buffer(Short.SIZE_BYTES + (Int.SIZE_BYTES * 10))
            var forceSend = false
            if ((chunkX / 8 == 48 || chunkX / 8 == 49) && chunkZ / 8 == 48) {
                forceSend = true
            }
            if (chunkX / 8 == 48 && chunkZ / 8 == 148) {
                forceSend = true
            }

            var count = 0
            buf.writeShort(count) // Client always read as unsigned short
            for (x in lx..rx) {
                for (z in lz..rz) {
                    if (!forceSend || z != 49 && z != 149 && z != 147 && x != 50 && (x != 49 || z != 47)) {
                        val region = z + (x shl 8)
                        val keys = message.xteaKeyService!!.get(region)
                        for (xteaKey in keys) {
                            buf.writeInt(xteaKey) // Client always reads as int
                        }
                    }
                    count++
                }
            }
            buf.setShort(0, count)

            val xteas = ByteArray(buf.readableBytes())
            buf.readBytes(xteas)
            xteas
        }
        else -> throw Exception("Unhandled value key.")
    }
}