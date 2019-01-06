package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcUpdateBlockSegment(val npc: Npc) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {

    }
}