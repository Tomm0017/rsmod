package gg.rsmod.plugins.content.skills.smithing.action

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.messageBox
import gg.rsmod.plugins.api.ext.playSound
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.prefixAn
import gg.rsmod.plugins.content.skills.smithing.data.Bar
import gg.rsmod.plugins.content.skills.smithing.data.SmithingMetaData

/**
 * @author Triston Plummer ("Dread")
 *
 * Handles the action of smithing a bar at a furnace
 */
object SmithingAction {

    /**
     * The message displayed when trying to smith a bar without the required level
     */
    private const val INSUFFICIENT_LEVEL_BAR = "You need a Smithing level of at least %d to work %ss."

    /**
     * The message displayed when trying to smith an item without the required level
     */
    private const val INSUFFICIENT_LEVEL_ITEM = "You need a Smithing level of %d to make %s."

    /**
     * The message displayed when trying to smith an item without the correct number of bars
     */
    private const val INSUFFICIENT_BAR_QTY = "You don't have enough %ss to make %s."

    /**
     * The message displayed when trying to smith a bar without a hammer
     */
    private const val NO_HAMMER = "You need a hammer to work the metal with."

    /**
     * The animation played when smithing an item
     */
    private const val SMITHING_ANIM = 898

    /**
     * The sound played when smithing an item
     */
    private const val SMITHING_ANVIL_SOUND = 3771

    /**
     * Handles the smithing of a bar
     *
     * @param task      The queued task
     * @param meta      The smithing meta data
     * @param amount    The amount to smith
     */
    suspend fun smith(task: QueueTask, meta: SmithingMetaData, amount: Int) {

        if (!canSmith(task, meta))
            return

        val player = task.player
        val inventory = player.inventory

        val barAmount = inventory.getItemCount(meta.bar!!.id)
        val maxAmount = (barAmount / meta.barCount)
        val craftAmount = Math.min(amount, maxAmount)

        repeat(craftAmount) {

            task.wait(2)

            player.animate(SMITHING_ANIM)
            player.playSound(SMITHING_ANVIL_SOUND)
            task.wait(3)

            if (!canSmith(task, meta)) {
                player.animate(-1)
                return@repeat
            }

            val transaction = inventory.remove(item = meta.bar.id, amount = meta.barCount, assureFullRemoval = true)
            if (transaction.hasSucceeded()) {
                inventory.add(meta.id, meta.numProduced)
                player.addXp(Skills.SMITHING, (meta.barCount * meta.bar.smithXp))
            }

        }
    }

    /**
     * Checks if the player has a hammer to smith with
     *
     * @param task  The queued task
     * @return      If the player has a hammer
     */
    private suspend fun hasHammer(task: QueueTask) : Boolean {
        val hammer = task.player.inventory.contains(Items.HAMMER)
        if (!hammer) {
            task.messageBox(NO_HAMMER)
        }
        return hammer
    }

    /**
     * Checks if the player can smith a specified bar
     *
     * @param task  The queued task
     * @param bar   The bar definition
     * @return      If the bar can be smithed by the player
     */
    suspend fun canSmithBar(task: QueueTask, bar: Bar?) : Boolean {
        val player = task.player

        if (bar == null) {
            task.messageBox("You should select an item from your inventory and use it on the anvil.")
            return false
        }

        if (bar.level > player.getSkills().getCurrentLevel(Skills.SMITHING)) {
            val barDef = task.player.world.definitions.get(ItemDef::class.java, bar.id)
            task.messageBox(INSUFFICIENT_LEVEL_BAR.format(bar.level, barDef.name.toLowerCase()))
            return false
        }

        return hasHammer(task)
    }

    /**
     * Checks if the player can smith an item
     *
     * @param task  The queued task
     * @param meta  The item meta data
     * @return      If the item can be smithed
     */
    private suspend fun canSmith(task: QueueTask, meta: SmithingMetaData) : Boolean {
        if (canSmithBar(task, meta.bar)) {
            val player = task.player

            if (meta.level > player.getSkills().getCurrentLevel(Skills.SMITHING)) {
                task.messageBox(INSUFFICIENT_LEVEL_ITEM.format(meta.level, meta.name.prefixAn()))
                return false
            }

            if (meta.barCount > player.inventory.getItemCount(meta.bar!!.id)) {
                val barDef = task.player.world.definitions.get(ItemDef::class.java, meta.bar.id)
                task.messageBox(INSUFFICIENT_BAR_QTY.format(barDef.name.toLowerCase(), meta.name.prefixAn()))
                return false
            }

            return true
        }

        return false
    }
}