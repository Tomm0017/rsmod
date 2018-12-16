package gg.rsmod.game.sync.task

import gg.rsmod.game.message.impl.ChangeStaticRegionMessage
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.Chunk
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPreSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    override fun execute() {
        player.movementQueue.pulse()

        var changeRegion = false

        if (player.lastKnownRegionBase == null) {
            val chunkX = player.tile.x shr 3
            val chunkZ = player.tile.z shr 3
            val regionX = (chunkX - (Chunk.MAX_VIEWPORT shr 4)) shl 3
            val regionZ = (chunkZ - (Chunk.MAX_VIEWPORT shr 4)) shl 3

            player.lastKnownRegionBase = Tile(regionX, regionZ, player.tile.height)
        } else {
            val last = player.lastKnownRegionBase!!
            val current = player.tile

            val dx = current.x - last.x
            val dz = current.z - last.z
            val changedHeight = current.height != last.height

            if (dx <= Player.VIEW_DISTANCE || dz <= Player.VIEW_DISTANCE || dx >= Chunk.MAX_VIEWPORT - Player.VIEW_DISTANCE - 1
                    || dz >= Chunk.MAX_VIEWPORT - Player.VIEW_DISTANCE - 1) {

                val chunkX = current.x shr 3
                val chunkZ = current.z shr 3
                val regionX = ((chunkX - (Chunk.MAX_VIEWPORT shr 4)) shl 3)
                val regionZ = ((chunkZ - (Chunk.MAX_VIEWPORT shr 4)) shl 3)

                player.lastKnownRegionBase = Tile(regionX, regionZ, current.height)
                player.write(ChangeStaticRegionMessage(chunkX, chunkZ, current))
            }
        }
    }
}