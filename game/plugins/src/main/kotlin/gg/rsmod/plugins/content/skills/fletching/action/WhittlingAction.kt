package gg.rsmod.plugins.content.skills.fletching.action

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.fletching.data.Log
import gg.rsmod.plugins.content.skills.fletching.data.WhittleItem

/**
 * @author momof513
 *
 * Handles the action of whittling logs with a knife
 */
class WhittlingAction(private val defs: DefinitionSet) {
    /**
     * A map of log ids to their item names
     */
    private val logNames = Log.logDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * A map of fletchable item ids to their item names
     */
    private val whittleNames = Log.logDefinitions.flatMap { it.value.values }.distinct().associate { it.id to defs.get(ItemDef::class.java, it.id).name.toLowerCase() }

    /**
     * Handles the whittling of a log
     *
     * @param task          The queued action task
     * @param log           The log definition
     * @param whittleItem   The whittleItem definition
     * @param amount        The amount the player is trying to whittle
     */
    suspend fun whittle(task: QueueTask, log: Int, whittleItem: WhittleItem, amount: Int) {
        if (!canWhittle(task, log, whittleItem))
            return

        val player = task.player
        val inventory = player.inventory

        val primaryCount = inventory.getItemCount(log)/whittleItem.logCount
        val maxCount = Math.min(amount, primaryCount)

        // Wait two ticks to follow OSRS behavior
        task.wait(2)
        var completed = 0
        while(completed < maxCount) {
            player.animate(WHITTLE_ANIM)
            task.wait(whittleItem.ticks)
            //player.playSound(WHITTLE_SOUND)

            player.lock()
            // This is here to prevent a TOCTTOU attack
            if (!canWhittle(task, log, whittleItem, sendMessageBox = false)){
                player.unlock()
                break
            }

            val removeLog = inventory.remove(item = log, amount = whittleItem.logCount, assureFullRemoval = true)
            if (removeLog.hasFailed()){
                player.unlock()
                break
            }

            var amountMade = whittleItem.amount

            // Ogre Arrow Shafts randomized between 2 and 6
            if(whittleItem.id == Items.OGRE_ARROW_SHAFT){
                amountMade = (2..6).random()
            }

            inventory.add(whittleItem.id, amount = amountMade)
            player.addXp(Skills.FLETCHING, whittleItem.fletchingXP * amountMade)
            completed++
            player.unlock()
        }
    }

    /**
     * Checks if a log can be whittled into the given whittleItem
     *
     * @param task              The queued task
     * @param log               The log id being whittled
     * @param whittleItem       The whittleItem being created
     * @param sendMessageBox    Whether or not to send the error message
     */
    private suspend fun canWhittle(task: QueueTask, log: Int, whittleItem: WhittleItem, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (inventory.getItemCount(log) < whittleItem.logCount) {
            if(sendMessageBox)
                task.messageBox("You need ${whittleItem.logCount} ${logNames[log]} to make a ${whittleNames[whittleItem.id]}")
            return false
        }

        if (!inventory.contains(Items.KNIFE)) {
            if(sendMessageBox)
                task.messageBox("You need a knife to cut ${logNames[log]} into ${whittleNames[whittleItem.id]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.FLETCHING) < whittleItem.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.FLETCHING)} level of at least ${whittleItem.level} to fletch ${whittleNames[whittleItem.id]}.")
            return false
        }

        return true
    }


    companion object {

        /**
         * The animation played when whittling a log
         */
        const val WHITTLE_ANIM = 1248

        /**
         * The sound played when whittling a log
         */
        const val WHITTLE_SOUND = -1
    }
}