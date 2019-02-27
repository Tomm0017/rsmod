package gg.rsmod.game.action

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerDeathAction {

    val deathPlugin: Plugin.() -> Unit = {
        val player = ctx as Player
        suspendable {
            death(it, player)
        }
    }

    private suspend fun death(it: Plugin, player: Player) {
    }
}