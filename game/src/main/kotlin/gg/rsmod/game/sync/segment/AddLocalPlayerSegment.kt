package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AddLocalPlayerSegment(private val other: Player, private val last18BitTileHash: Int, private val curr18BitTileHash: Int) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        val updateLocation = PlayerLocationHashSegment(last18BitTileHash, curr18BitTileHash)

        /**
         * Signal to the client that the player needs to be decoded.
         */
        buf.putBits(1, 1)
        /**
         * Signal to the client that the non-local player needs to be added
         * as a local player.
         */
        buf.putBits(2, 0)
        /**
         * Signal to the client that the player needs to have their location
         * decoded.
         */
        buf.putBits(1, 1)
        /**
         * Encode the player's location hash.
         */
        updateLocation.encode(buf)

        buf.putBits(13, other.tile.x and 0x1FFF)
        buf.putBits(13, other.tile.z and 0x1FFF)
        buf.putBits(1, 1) // Requires block update
    }
}