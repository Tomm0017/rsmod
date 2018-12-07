package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Player
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerPostSynchronizationTask(val player: Player, override val phaser: Phaser) : PhasedSynchronizationTask(phaser) {

    override fun execute() {
        player.teleport = false
        player.lastTile = Tile(player.tile)
        player.blockBuffer.clean()
    }
}