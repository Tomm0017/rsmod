package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

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
     * The sound effect played when crafting a const
     */
    private const val RUNECRAFT_SOUND = 2710

    /**
     * Handles the pre-crafting action (the animation, graphics, and lock)
     *
     * @param it    The queued action task
     */
    private suspend fun preCraft(it: QueueTask) {
        val player = it.player

        player.lock()

        player.animate(RUNECRAFT_ANIM)
        player.graphic(RUNECRAFT_GRAPHIC)
        player.playSound(RUNECRAFT_SOUND)

        it.wait(RUNECRAFT_WAIT_CYCLE)

        player.unlock()
    }

    /**
     * Handles the crafting of a combination rune
     *
     * @param it    The queued action task
     * @param combo The combination rune to craft
     */
    suspend fun craftCombination(it: QueueTask, combo: CombinationRune) {
        val player = it.player
        val world = player.world

        if (!canCraftCombo(it, combo))
            return

        preCraft(it)

        val inventory = player.inventory
        val count = Math.min(inventory.getItemCount(Items.PURE_ESSENCE), inventory.getItemCount(combo.rune))

        val removeTalismanTrans = inventory.remove(combo.talisman)

        if (removeTalismanTrans.hasSucceeded()) {

            val removeEssTrans = inventory.remove(item = Items.PURE_ESSENCE, amount = count)
            val removeRuneTrans = inventory.remove(item = combo.rune, amount = removeEssTrans.items.size)

            if (removeRuneTrans.hasSucceeded()) {
                val runeCount = if (player.hasEquipped(EquipmentType.AMULET, Items.BINDING_NECKLACE)) count else world.random(IntRange(start = 1, endInclusive = count))

                inventory.add(item = combo.id, amount = runeCount)
                player.addXp(Skills.RUNECRAFTING, runeCount * combo.xp)
            }
        }
    }

    /**
     * Handles the action of crafting a rune from essence
     *
     * @param it    The queued task instance
     * @param rune  The rune being crafted
     */
    suspend fun craftRune(it: QueueTask, rune: Rune) {
        val player = it.player

        if (!canCraftRune(it, rune))
            return

        preCraft(it)

        val inventory = player.inventory
        val essence = inventory.filter { it != null }.map { it?.id!! }.first { rune.essence.contains(it) }
        val transaction = inventory.remove(item = essence, amount = inventory.getItemCount(essence))

        val count = (transaction.items.size * rune.getBonusMultiplier(player.getSkills().getMaxLevel(Skills.RUNECRAFTING))).toInt()

        if (transaction.hasSucceeded()) {
            player.inventory.add(rune.id, count)
            player.addXp(Skills.RUNECRAFTING, rune.xp * count)
        }
    }

    /**
     * Checks if a player can craft a specified rune
     *
     * @param it    The queued task instance
     * @param rune  The rune being crafted
     */
    private suspend fun canCraftRune(it: QueueTask, rune: Rune) : Boolean {
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

    /**
     * Checks if a player can craft a combination rune
     *
     * @param it    The queued action task
     * @param combo The combination rune to craft
     */
    private suspend fun canCraftCombo(it: QueueTask, combo: CombinationRune) : Boolean {
        val player = it.player

        val comboName = player.world.definitions.get(ItemDef::class.java, combo.id).name.toLowerCase()
        val runeName = player.world.definitions.get(ItemDef::class.java, combo.rune).name.toLowerCase()
        val talismanName = player.world.definitions.get(ItemDef::class.java, combo.talisman).name.toLowerCase()

        if (player.getSkills().getMaxLevel(Skills.RUNECRAFTING) < combo.level) {
            it.messageBox("You need a ${Skills.getSkillName(player.world, Skills.RUNECRAFTING)} level of at least ${combo.level} to craft ${comboName}s.")
            return false
        }

        if (!player.inventory.contains(Items.PURE_ESSENCE)) {
            player.message("You need pure essence to bind ${comboName}s.")
            return false
        }

        if (!player.inventory.contains(combo.rune)) {
            player.message("You need ${runeName}s to bind ${comboName}s.")
            return false
        }

        if (!player.inventory.contains(combo.talisman)) {
            player.message("You need a $talismanName to bind ${comboName}s.")
            return false
        }

        return true
    }
}