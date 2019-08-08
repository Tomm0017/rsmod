package gg.rsmod.plugins.content.skills.crafting
/**
 *TO-DO
 * crushed gem for OPAL JADE & REDTOPAZ in a interpolate
 * abilty to change "you need crafting lvl to cut this" to "you need crafting lvl to cut "gemname"" same with cutting gem
 * rest of crafting this is just basic gem cutting
 */
import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.crafting.data.Gems

object Cutting {

    suspend fun gemCut(it: QueueTask, gem: Gems) {
        if(!canCut(it.player, gem)) {
            return
        }
        val player = it.player
        val inventory = player.inventory
        val removeuncut = inventory.remove(item = gem.uncutGem)

        if (removeuncut.hasSucceeded()) {
            inventory.add(gem.id)
        }

        player.filterableMessage("You cut the ${gem.prefix}.")

        while(true) {
            player.lock = LockState.DELAY_ACTIONS
            player.addXp(Skills.CRAFTING, gem.craftXp)
            player.animate(gem.animation)
            it.wait(5)
            player.lock = LockState.NONE

            if(!canCut(player,gem)) {
                player.animate(-1)
                break
            }
                player.animate(-1)

                break
            }
            it.wait(1)
        }
    }

    private fun canCut(player: Player, gem: Gems): Boolean {
        if(player.getSkills().getCurrentLevel(Skills.CRAFTING) < gem.level) {
            player.filterableMessage("You need a Crafting level of ${gem.level} to cut ${gem.prefix}.")
            return false
        }

        return true
    }