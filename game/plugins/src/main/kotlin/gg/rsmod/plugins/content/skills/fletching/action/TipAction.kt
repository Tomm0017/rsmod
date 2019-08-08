package gg.rsmod.plugins.content.skills.fletching.action

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.fletching.data.Tipped

/**
 * @author momof513
 *
 * Handles the action of adding tips to an item
 */
class TipAction(private val defs: DefinitionSet) {

    /**
     * A map of the completed items to their item names
     */
    private val itemNames = Tipped.tippedDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * A map of tipped item bases to their item names
     */
    private val baseNames = Tipped.tippedDefinitions.values.associate { it.base to defs.get(ItemDef::class.java, it.base).name.toLowerCase() }

    /**
     * A map of item tips to their item names
     */
    private val tipNames = Tipped.tippedDefinitions.values.associate { it.tip to defs.get(ItemDef::class.java, it.tip).name.toLowerCase() }

    /**
     * Handles the tipping of an item
     *
     * @param task      The queued action task
     * @param tipped    The tipped definition
     * @param amount    The amount the player is trying to tip
     */
    suspend fun tip(task: QueueTask, tipped: Tipped, amount: Int) {
        if (!canTip(task, tipped))
            return

        val player = task.player
        val inventory = player.inventory

        val baseCount = Math.ceil(inventory.getItemCount(tipped.base) / tipped.setAmount.toDouble()).toInt()
        val tipCount = Math.ceil(inventory.getItemCount(tipped.tip) / tipped.setAmount.toDouble()).toInt()
        val maxCount = Math.min(Math.min(amount, baseCount), tipCount)

        var completed = 0
        while(completed < maxCount) {
            task.wait(2)

            player.lock()
            // This is here again to prevent a TOCTTOU attack
            if (!canTip(task, tipped, sendMessageBox = false)){
                player.unlock()
                break
            }

            val currentBaseCount = inventory.getItemCount(tipped.base)
            val currentTipCount = inventory.getItemCount(tipped.tip)
            val currentSetAmount = Math.min(Math.min(currentBaseCount, currentTipCount), tipped.setAmount)
            val currentBaseIndex = inventory.getItemIndex(tipped.base, true)

            val removeBases = inventory.remove(item = tipped.base, amount = currentSetAmount, assureFullRemoval = true)
            if (removeBases.hasFailed()){
                player.unlock()
                break
            }

            val removeTips = inventory.remove(item = tipped.tip, amount = currentSetAmount, assureFullRemoval = true)
            if (removeTips.hasFailed()){
                inventory.add(item = tipped.base, amount = currentSetAmount, beginSlot = currentBaseIndex)
                player.unlock()
                break
            }

            inventory.add(tipped.id, amount = currentSetAmount)
            player.addXp(Skills.FLETCHING, tipped.fletchingXP * currentSetAmount)
            completed++
            player.unlock()
        }
    }

    /**
     * Checks if the user has the required fletching level and at least one item base and tip
     *
     * @param task              The queued task
     * @param tipped            The Tipped definition
     * @param sendMessageBox    Whether or not to send the error message
     */
    private suspend fun canTip(task: QueueTask, tipped: Tipped, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (inventory.getItemCount(tipped.base) < 1 || inventory.getItemCount(tipped.tip) < 1) {
            if(sendMessageBox)
                task.messageBox("You need ${baseNames[tipped.base]} and a ${tipNames[tipped.tip]} to make a ${itemNames[tipped.id]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.FLETCHING) < tipped.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.FLETCHING)} level of at least ${tipped.level} to tip ${itemNames[tipped.id]}.")
            return false
        }

        return true
    }
}