package gg.rsmod.game.action

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerDeathAction {

    val deathPlugin: Plugin.() -> Unit = {
        val player = ctx as Player
        player.queue {
            death(player)
        }
    }

    private suspend fun QueueTask.death(player: Player) {
    }
}