package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcSkipSegment(private val skip: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        buf.putBits(1, if (skip) 0 else 1)
    }
}