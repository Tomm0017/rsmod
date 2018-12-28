package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPostSynchronizationTask(val player: Player) : SynchronizationTask {

    override fun run() {
        player.teleport = false
        player.lastTile = Tile(player.tile)
        player.steps = null
        player.blockBuffer.clean()
    }
}