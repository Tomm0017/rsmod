package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPostSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    override fun execute() {
        val oldTile = player.lastTile
        val newTile = player.tile

        player.teleport = false
        player.lastTile = Tile(player.tile)
        player.steps = null
        player.blockBuffer.clean()

        if (oldTile == null || !oldTile.sameAs(newTile)) {
            if (oldTile != null && !oldTile.sameAs(0, 0)) {
                val oldChunk = player.world.chunks.getForTile(oldTile)
                oldChunk.removeEntity(player.world, player, oldTile)
            }
            if (!newTile.sameAs(0, 0)) {
                val newChunk = player.world.chunks.getForTile(newTile)
                newChunk.addEntity(player.world, player, newTile)
            }
        }
    }
}