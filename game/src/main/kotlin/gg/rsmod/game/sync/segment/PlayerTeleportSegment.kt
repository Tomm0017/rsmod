package gg.rsmod.game.sync.segment

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerTeleportSegment(private val player: Player, private val other: Player, private val index: Int, private val encodeUpdateBlocks: Boolean) : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        /**
         * Signal to the client that [other] needs to be decoded.
         */
        buf.putBits(1, 1)
        /**
         * Does [other] have pending [gg.rsmod.game.sync.UpdateBlockType]s?
         */
        buf.putBit(encodeUpdateBlocks)
        /**
         * Signal to the client that [other] has been moved without actual
         * walking being involved.
         */
        buf.putBits(2, 3)

        /**
         * The difference from [other]'s last tile as far as [player]'s client is
         * concerned.
         */
        var dx = other.tile.x - ((player.otherPlayerTiles[index] shr 14) and 0x3FFF)
        var dz = other.tile.z - ((player.otherPlayerTiles[index] and 0x3FFF))
        val dh = other.tile.height - ((player.otherPlayerTiles[index] shr 28) and 0x3)

        /**
         * If the move is within a short radius, we want to save some bandwidth.
         */
        if (Math.abs(dx) <= 14 && Math.abs(dz) <= 14) {
            if (dx < 0) {
                dx += 32
            }
            if (dz < 0) {
                dz += 32
            }
            /**
             * Signal to the client that the difference in tiles are within
             * viewing distance.
             */
            buf.putBits(1, 0)
            /**
             * Write the difference in tiles.
             */
            buf.putBits(12, (dz and 0x1F) or ((dx and 0x1F) shl 5) or (dh and 0x3) shl 10)
        } else {
            /**
             * Signal to the client that the difference in tiles are not within
             * viewing distance.
             */
            buf.putBits(1, 1)
            /**
             * Write the difference in tiles.
             */
            buf.putBits(30, (dz and 0x3FFF) or ((dx and 0x3FFF) shl 14) or (dh and 0x3) shl 28)
        }
    }
}