package gg.rsmod.plugins.content.skills.fletching.action

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.fletching.data.Attached

/**
 * @author momof513
 *
 * Handles the action of attaching one item to another (with or without a required third item)
 */
class AttachAction(private val defs: DefinitionSet) {

    /**
     * A map of attached items to their item names
     */
    private val itemNames = Attached.attachedDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * A map of material ids to their item names
     */
    private val materialNames = Attached.attachedDefinitions.values.flatMap { listOf(it.firstMaterial, it.secondMaterial) }.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * Handles the attachment of item one to item two
     *
     * @param task      The queued action task
     * @param attached  The attached definition
     * @param amount    The amount of items the player is trying to make
     */
    suspend fun attach(task: QueueTask, attached: Attached, amount: Int) {
        if (!canAttach(task, attached))
            return

        val player = task.player
        val inventory = player.inventory

        val firstMaterialCount = inventory.getItemCount(attached.firstMaterial)
        val secondMaterialCount = inventory.getItemCount(attached.secondMaterial)
        val maxCount = Math.min(Math.min(amount, firstMaterialCount), secondMaterialCount)

        // Wait two ticks to follow OSRS behavior
        task.wait(2)
        var completed = 0
        while(completed < maxCount) {
            player.animate(attached.animation)
            task.wait(2)

            player.lock()
            // This is here again to prevent a TOCTTOU attack
            if (!canAttach(task, attached, sendMessageBox = false)){
                player.unlock()
                break
            }

            val firstMaterialIndex = inventory.getItemIndex(attached.firstMaterial, true)
            val removeFirstMaterial = inventory.remove(item = attached.firstMaterial, assureFullRemoval = true)
            if (removeFirstMaterial.hasFailed()){
                player.unlock()
                break
            }

            val removeSecondMaterial = inventory.remove(item = attached.secondMaterial, assureFullRemoval = true)
            if (removeSecondMaterial.hasFailed()){
                inventory.add(item = attached.firstMaterial, beginSlot = firstMaterialIndex)
                player.unlock()
                break
            }

            inventory.add(attached.id)
            player.addXp(Skills.FLETCHING, attached.fletchingXP)
            completed++
            player.unlock()
        }
    }

    /**
     * Checks if the user has the required level, one of each item, and the required tool (if any).
     *
     * @param task              The queued task
     * @param attached          The attached definition
     * @param sendMessageBox    Whether or not to send the error message
     */
    private suspend fun canAttach(task: QueueTask, attached: Attached, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (inventory.getItemCount(attached.firstMaterial) < 1 || inventory.getItemCount(attached.secondMaterial) < 1) {
            if (sendMessageBox)
                task.messageBox("You need ${materialNames[attached.firstMaterial]} and a ${materialNames[attached.secondMaterial]} to make a ${itemNames[attached.id]}")
            return false
        }

        if (attached.toolRequired >= 0 && !inventory.contains(attached.toolRequired)) {
            if(sendMessageBox)
                task.messageBox("You need a ${defs.get(ItemDef::class.java, attached.toolRequired).name.toLowerCase()} to attach ${materialNames[attached.firstMaterial]} and ${materialNames[attached.secondMaterial]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.FLETCHING) < attached.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.FLETCHING)} level of at least ${attached.level} to fletch ${itemNames[attached.id]}.")
            return false
        }

        return true
    }
}