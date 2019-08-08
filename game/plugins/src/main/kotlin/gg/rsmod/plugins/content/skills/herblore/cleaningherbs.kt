package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.herblore.data.Grimy

object Cleaning {

    suspend fun Grimy(it: QueueTask, Grimy: Grimy) {
        if(!canclean(it.player, Grimy)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removegrimy = inventory.remove(item = Grimy.grimyherb)

        if (removegrimy.hasSucceeded()) {
            inventory.add(Grimy.herb)
        }

        while(true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.HERBLORE, Grimy.herbloreXp)
            //player.animate(Spin.animation)
            //it.wait(5)
            player.lock = LockState.NONE

            if(!canclean(player,Grimy)) {
                player.animate(-1)
                break
            }
                player.animate(-1)

                break
            }
            it.wait(1)
        }
    }

    private fun canclean(player: Player, Grimy: Grimy): Boolean {
        if(player.getSkills().getCurrentLevel(Skills.HERBLORE) < Grimy.level) {
            player.filterableMessage("You need a Herblore level of ${Grimy.level} to clean ${Grimy.prefix}.")
            return false
        }

        return true
    }