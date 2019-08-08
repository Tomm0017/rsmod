package gg.rsmod.plugins.content.skills.fletching.action

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.fletching.data.Feathered

/**
 * @author momof513
 *
 * Handles the action of adding feathers to an item
 */
class FeatherAction(private val defs: DefinitionSet) {

    /**
     * A map of feathered items to their item names
     */
    private val itemNames = Feathered.featheredDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * A map of the unfeathered items to their item names
     */
    private val unfeatheredNames = Feathered.featheredDefinitions.values.associate { it.unfeathered to defs.get(ItemDef::class.java, it.unfeathered).name.toLowerCase() }

    /**
     * A map of feathers to their item names
     */
    private val featherNames = Feathered.feathers.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * Handles the attachment of feathers to a item
     *
     * @param task      The queued action task
     * @param feathered The feathered item definition
     * @param feather   The item ID of the type of feather being used
     */
    suspend fun feather(task: QueueTask, feathered: Feathered, feather: Int, amount: Int) {
        val player = task.player
        val inventory = player.inventory

        var completed = 0
        do {
            // Pause 2 ticks between each iteration if im making headless arrows (Only case where amount > 1)
            if(feathered.id == Items.HEADLESS_ARROW || feathered.id == Items.FLIGHTED_OGRE_ARROW)
                task.wait(2)

            player.lock()
            if (!canFeather(task, feathered, feather, sendMessageBox = (completed == 0))) {
                player.unlock()
                return
            }

            val unfeatheredCount = inventory.getItemCount(feathered.unfeathered)
            val featherCount = inventory.getItemCount(feather)
            val amountToFeather = Math.min(Math.min(unfeatheredCount, (featherCount/feathered.feathersNeeded)), feathered.amount)
            val feathersUsed = feathered.feathersNeeded * amountToFeather

            val unfeatheredIndex = inventory.getItemIndex(feathered.unfeathered, true)
            val removeUnfeathered = inventory.remove(item = feathered.unfeathered, amount = amountToFeather, assureFullRemoval = true)
            if (removeUnfeathered.hasFailed()) {
                player.unlock()
                return
            }

            val removeFeather = inventory.remove(item = feather, amount = feathersUsed, assureFullRemoval = true)
            if (removeFeather.hasFailed()) {
                inventory.add(item = feathered.unfeathered, amount = amountToFeather, beginSlot = unfeatheredIndex)
                player.unlock()
                return
            }

            inventory.add(feathered.id, amount = amountToFeather)
            player.addXp(Skills.FLETCHING, feathered.fletchingXP * amountToFeather)
            player.unlock()
            completed++
        } while (completed < amount)
    }

    /**
     * Checks if the user has the required level, and at least one feather & unfeathered item
     *
     * @param task      The queued task
     * @param feathered The feathered definition
     * @param feather   The feather ID of the type of feather being used
     */
    private suspend fun canFeather(task: QueueTask, feathered: Feathered, feather: Int, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (inventory.getItemCount(feathered.unfeathered) < 1 || inventory.getItemCount(feather) < feathered.amount) {
            if(sendMessageBox)
                task.messageBox("You need 1 ${unfeatheredNames[feathered.unfeathered]} and ${feathered.amount} ${featherNames[feather]} to make a ${itemNames[feathered.id]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.FLETCHING) < feathered.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.FLETCHING)} level of at least ${feathered.level} to fletch ${itemNames[feathered.id]}.")
            return false
        }

        return true
    }
}