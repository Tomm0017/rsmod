package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcWalkSegment(private val walkDirection: Int, private val runDirection: Int,
                     private val decodeUpdateBlocks: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        val running = runDirection != -1
        buf.putBits(2, if (running) 2 else 1)
        buf.putBits(3, walkDirection)
        if (running) {
            buf.putBits(3, runDirection)
        }
        buf.putBits(1, if (decodeUpdateBlocks) 1 else 0)
    }
}