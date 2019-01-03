package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerLocationHashSegment(val src: Int, val dst: Int) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        val srcX = (src shr 14) and 0x3FFF
        val srcZ = src and 0x3FFF
        val srcH = (src shr 28) and 0x3

        val dstX = (dst shr 14) and 0x3FFF
        val dstZ = dst and 0x3FFF
        val dstH = (dst shr 28) and 0x3

        val dh = dstH - srcH
        if (srcX == dstX && srcZ == dstZ) {
            // Assume there's only a height difference as we checked
            // that the hashes did not match.
            buf.putBits(2, 1)
            buf.putBits(2, dh and 0x3)
        } else if (Math.abs(dstX - srcX) <= 1 && Math.abs(dstZ - srcZ) <= 1) {
            // If we only moved a tile.
            val direction: Int
            val dx = dstX - srcX
            val dy = dstZ - srcZ
            if (dx == -1 && dy == -1)
                direction = 0
            else if (dx == 1 && dy == -1)
                direction = 2
            else if (dx == -1 && dy == 1)
                direction = 5
            else if (dx == 1 && dy == 1)
                direction = 7
            else if (dy == -1)
                direction = 1
            else if (dx == -1)
                direction = 3
            else if (dx == 1)
                direction = 4
            else
                direction = 6
            buf.putBits(2, 2)
            buf.putBits(5, ((dh and 0x3) shl 3) or (direction and 0x7))
        } else {
            // If we moved further.
            val dx = Math.floor((dstX / 0x1FFF).toDouble()).toInt()
            val dz = Math.floor((dstZ / 0x1FFF).toDouble()).toInt()
            buf.putBits(2, 3)
            buf.putBits(18, (dz and 0xFF) or ((dx and 0xFF) shl 8) or ((dh and 0x3) shl 16))
        }
    }
}