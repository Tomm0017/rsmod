package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.herblore.data.Potions

object Potionmaking {

    suspend fun unfinished(it: QueueTask, pot: Potions) {
        if (!canPot(it.player, pot)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removefirst = inventory.remove(item = pot.firstingreident)

        if (removefirst.hasSucceeded()) {
            inventory.remove(Items.VIAL_OF_WATER, 1)
            inventory.add(pot.unfinished)
        }

        while (true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.HERBLORE, pot.herbloreXp)
            player.lock = LockState.NONE

            if (!canPot(player, pot)) {
                player.animate(-1)
                break
            }
            player.animate(-1)

            break
        }
        it.wait(1)
    }

    suspend fun finished(it: QueueTask, pot: Potions) {
        if (!canPot(it.player, pot)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removesecond = inventory.remove(item = pot.secondingreident)

        if (removesecond.hasSucceeded()) {
            inventory.remove(pot.unfinished)
            inventory.add(pot.potion)
        }

        while (true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.HERBLORE, pot.herbloreXp)
            player.lock = LockState.NONE

            if (!canPot(player, pot)) {
                player.animate(-1)
                break
            }
            player.animate(-1)

            break
        }
        it.wait(1)
    }
}

    private fun canPot(player: Player, pot: Potions): Boolean {
        if(player.getSkills().getCurrentLevel(Skills.HERBLORE) < pot.level) {
            player.filterableMessage("You need a Herblore level of ${pot.level} to make ${pot.prefix}.")
            return false
        }

        return true
    }