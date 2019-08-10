package gg.rsmod.plugins.content.skills.crafting.action

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.crafting.data.LeatherItem
import gg.rsmod.plugins.content.skills.crafting.data.Leathers

/**
 * @author momof513
 * @editor pitch blac23
 */

class LeatherAction(private val defs: DefinitionSet) {

    private val leatherNames = Leathers.leatherDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }
    private val leatherItemNames = Leathers.leatherDefinitions.flatMap { it.value.values }.distinct().associate { it.id to defs.get(ItemDef::class.java, it.id).name.toLowerCase() }

    suspend fun leathers(task: QueueTask, leathers: Int, leatherItem: LeatherItem, amount: Int) {
        if (!canLeather(task, leathers, leatherItem))
            return

        val player = task.player
        val inventory = player.inventory

        val primaryCount = inventory.getItemCount(leathers)/leatherItem.leatherCount
        val maxCount = Math.min(amount, primaryCount)

        var completed = 0
        while(completed < maxCount) {
            player.animate(LEATHER_ANIM)
            task.wait(leatherItem.ticks)
            player.lock()

            if (!canLeather(task, leathers, leatherItem, sendMessageBox = false)){
                player.unlock()
                break
            }

            val removeLeather = inventory.remove(item = leathers, amount = leatherItem.leatherCount, assureFullRemoval = true)
            if (removeLeather.hasFailed()){
                player.unlock()
                break
            }

            var amountMade = leatherItem.amount

            inventory.add(leatherItem.id, amount = amountMade)
            player.addXp(Skills.CRAFTING, leatherItem.craftXp * amountMade)
            completed++
            player.unlock()
        }
    }

    private suspend fun canLeather(task: QueueTask, leathers: Int, leatherItem: LeatherItem, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (inventory.getItemCount(leathers) < leatherItem.leatherCount) {
            if(sendMessageBox)
                task.messageBox("You need ${leatherItem.leatherCount} ${leatherNames[leathers]} to make a ${leatherItemNames[leatherItem.id]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.CRAFTING) < leatherItem.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.CRAFTING)} level of at least ${leatherItem.level} to fletch ${leatherItemNames[leatherItem.id]}.")
            return false
        }

        return true
    }
    companion object {

        const val LEATHER_ANIM = 1249
    }
}