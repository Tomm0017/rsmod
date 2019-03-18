package gg.rsmod.game.sync.task

import gg.rsmod.game.message.impl.RebuildNormalMessage
import gg.rsmod.game.message.impl.RebuildRegionMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.Coordinate
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

        val last = player.lastKnownRegionBase
        val current = player.tile

        if (last == null || shouldRebuildRegion(last, current)) {
            val regionX = ((current.x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = ((current.z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3

            player.lastKnownRegionBase = Coordinate(regionX, regionZ, current.height)

            val xteaService = player.world.xteaKeyService
            val instance = player.world.instanceAllocator.getMap(current)
            val rebuildMessage = when {
                instance != null -> RebuildRegionMessage(current.x shr 3, current.z shr 3, 1, instance.getCoordinates(player.tile), xteaService)
                else -> RebuildNormalMessage(current.x shr 3, current.z shr 3, xteaService)
            }
            player.write(rebuildMessage)
        }
    }

    private fun shouldRebuildRegion(old: Coordinate, new: Tile): Boolean {
        val dx = new.x - old.x
        val dz = new.z - old.z

        return dx <= Player.NORMAL_VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
                || dz <= Player.NORMAL_VIEW_DISTANCE || dz >= Chunk.MAX_VIEWPORT - Player.NORMAL_VIEW_DISTANCE - 1
    }
}