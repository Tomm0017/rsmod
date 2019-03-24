package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.messageBox
import gg.rsmod.plugins.api.ext.player

/**
 * @author Triston Plummer ("Dread")
 *
 * Handles the action of crafting runes at an altar
 */
object RunecraftAction {

    /**
     * The animation played when crafting a rune
     */
    private const val RUNECRAFT_ANIM = 791

    /**
     * The number of ticks between playing the animation, and receiving the runes
     */
    private const val RUNECRAFT_WAIT_CYCLE = 3

    /**
     * The graphic played when crafting a rune
     */
    private val RUNECRAFT_GRAPHIC = Graphic(id = 186, height = 100)

    /**
     * Handles the action of crafting a rune from essence
     *
     * @param it    The queued task instance
     * @param rune  The rune being crafted
     */
    suspend fun craftRune(it: QueueTask, rune: Rune) {
        val player = it.player

        if (!canCraft(it, rune))
            return

        player.lock()

        player.animate(RUNECRAFT_ANIM)
        player.graphic(RUNECRAFT_GRAPHIC)

        it.wait(RUNECRAFT_WAIT_CYCLE)

        val inventory = player.inventory
        val essence = inventory.filter { it != null }.map { it?.id!! }.first { rune.essence.contains(it) }
        val transaction = inventory.remove(item = essence, amount = inventory.getItemCount(essence))

        val count = transaction.items.size

        if (transaction.hasSucceeded()) {
            player.inventory.add(rune.id, count)
            player.addXp(Skills.RUNECRAFTING, rune.xp * count)
        }

        player.unlock()
    }

    /**
     * Checks if a player can craft a specified rune
     *
     * @param it    The queued task instance
     * @param rune  The rune being crafted
     */
    private suspend fun canCraft(it: QueueTask, rune: Rune) : Boolean {
        val player = it.player
        val def = player.world.definitions.get(ItemDef::class.java, rune.id)

        if (player.getSkills().getMaxLevel(Skills.RUNECRAFTING) < rune.level) {
            it.messageBox("You need a ${Skills.getSkillName(player.world, Skills.RUNECRAFTING)} level of at least ${rune.level} to craft ${def.name.toLowerCase()}s.")
            return false
        }

        val essence = rune.essence
        val essenceDef = player.world.definitions.get(ItemDef::class.java, essence.first())

        if (!player.inventory.filter { it != null && essence.contains(it.id) }.any()) {
            it.messageBox("You do not have any ${essenceDef.name.toLowerCase()}s to bind.")
            return false
        }

        return true
    }
}