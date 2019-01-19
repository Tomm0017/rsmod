package gg.rsmod.game.action

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerDeathAction {

    val deathPlugin: (Plugin) -> Unit = {
        it.suspendable {
            death(it, it.ctx as Player)
        }
    }

    private suspend fun death(it: Plugin, player: Player) {

    }
}