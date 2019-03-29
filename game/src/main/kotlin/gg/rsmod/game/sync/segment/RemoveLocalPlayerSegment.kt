package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RemoveLocalPlayerSegment(private val updateTileHash: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        /*
         * Signal to the client that the player needs to be decoded.
         */
        buf.putBits(1, 1)
        /*
         * Signal to the client that the player does not require
         * [gg.rsmod.game.sync.block.UpdateBlockType] decoding.
         */
        buf.putBits(1, 0)
        /*
         * Signal to the client that the player needs to be removed.
         */
        buf.putBits(2, 0)
        /*
         * Signal to the client that the player's location does not need to be
         * decoded.
         */
        buf.putBits(1, if (updateTileHash) 1 else 0)
    }
}