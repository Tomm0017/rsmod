package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerLocationHashSegment(private val lastHash: Int, private val currHash: Int) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        val lastX = (lastHash shr 8) and 0xFF
        val lastZ = lastHash and 0xFF
        val lastH = lastHash shr 16

        val currX = (currHash shr 8) and 0xFF
        val currZ = currHash and 0xFF
        val currH = currHash shr 16

        val diffX = currX - lastX
        val diffZ = currZ - lastZ
        val diffH = (currH - lastH) and 0x3

        if (lastX == currX && lastZ == currZ) {
            // Assume there's only a height difference as we checked
            // that the hashes did not match.
            buf.putBits(2, 1)
            buf.putBits(2, diffH)
        } else if (Math.abs(diffX) <= 1 && Math.abs(diffZ) <= 1) {
            // If we only moved a tile.
            val direction: Int
            val dx = currX - lastX
            val dy = currZ - lastZ

            if (dx == -1 && dy == -1) {
                direction = 0
            } else if (dx == 1 && dy == -1) {
                direction = 2
            } else if (dx == -1 && dy == 1) {
                direction = 5
            } else if (dx == 1 && dy == 1) {
                direction = 7
            } else if (dy == -1) {
                direction = 1
            } else if (dx == -1) {
                direction = 3
            } else if (dx == 1) {
                direction = 4
            } else {
                direction = 6
            }

            buf.putBits(2, 2)
            buf.putBits(2, diffH)
            buf.putBits(3, direction)
        } else {
            // If we moved further.
            buf.putBits(2, 3)
            buf.putBits(2, diffH)
            buf.putBits(8, diffX and 0xFF)
            buf.putBits(8, diffZ and 0xFF)
        }
    }
}