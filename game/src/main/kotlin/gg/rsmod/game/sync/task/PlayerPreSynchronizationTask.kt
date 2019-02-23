package gg.rsmod.game.sync.task

import gg.rsmod.game.message.impl.RebuildNormalMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPreSynchronizationTask(val player: Player) : SynchronizationTask {

    override fun run() {
        player.handleFutureRoute()
        player.movementQueue.cycle()

        if (player.lastKnownRegionBase == null) {
            val regionX = ((player.tile.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((player.tile.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            player.lastKnownRegionBase = Tile(regionX, regionZ, player.tile.height)
        }

        val last = player.lastKnownRegionBase!!
        val current = player.tile

        if (shouldRebuildRegion(last, current)) {
            val regionX = ((current.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((current.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3

            player.lastKnownRegionBase = Tile(regionX, regionZ, current.height)
            player.write(RebuildNormalMessage(current.x, current.z, player.world.xteaKeyService))
        }
    }

    private fun shouldRebuildRegion(old: Tile, new: Tile): Boolean {
        val dx = new.x - old.x
        val dz = new.z - old.z

        return dx <= Player.NORMAL_VIEW_DISTANCE || dz <= Player.NORMAL_VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
                || dz >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
    }
}