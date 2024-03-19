package gg.rsmod.game.sync.segment

import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerWalkSegment(private val encodeUpdateBlocks: Boolean, private val running: Boolean, private val direction: Int) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        /*
         * Signal to the client that the player needs to be decoded.
         */
        buf.putBits(1, 1)
        /*
         * Does the player need to have their [gg.rsmod.game.sync.block.UpdateBlockType]s
         * decoded?
         */
        buf.putBit(encodeUpdateBlocks)
        /*
         * Is the player walking or running?
         */
        buf.putBits(2, if (running) 2 else 1) // Whether client should decode a walk or a run movement
        /*
         * Signal to the client what the direction of the movement is.
         */
        buf.putBits(if (running) 4 else 3, direction)
    }
}