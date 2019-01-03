package gg.rsmod.game.sync

import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface SynchronizationSegment {

    fun encode(buf: GamePacketBuilder)
}