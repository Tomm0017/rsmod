package gg.rsmod.plugins.content.skills.herblore.pots

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import mu.KLogging

class Pot(val unfinished: Int, val secondary: Int, val finished: IntArray, val minLevel: Int, val xpAwarded: Double) {
    fun make(player: Player) {
        if(player.replaceItemAndRemoveAnother(unfinished, finished[2], secondary.toItem())){
            player.animate(id = 363, delay = 0)
            player.playSound(2608, 1, 0)
            player.addXp(Skills.HERBLORE, xpAwarded)
        }
    }

    fun charge(player: Player) {
        val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
        val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!
        val chargeCount = finished.indexOf(player.inventory[dstSlot]!!.id)+1
        if(chargeCount > 3) {
            player.nothingMessage()
        } else if(chargeCount > 0) {
            consume(player)
            if(player.inventory.remove(finished[chargeCount-1], beginSlot = srcSlot).hasSucceeded())
                player.inventory.add(finished[chargeCount+1])

        } else { // all other values are invalid for charging pots
            // could only happen from improperly managed pot
            KLogging().logger.error("invalid chargeCount for pot at ${player.inventory[dstSlot]} with charges = $chargeCount")
        }
    }

    fun consume(player: Player) {
        val chargeCount = finished.indexOf(player.getInteractingItem().id)+1
        if(chargeCount == 1 && player.inventory.remove(finished[0], beginSlot = player.getInteractingItemSlot()).hasSucceeded()) {
            player.message("You drink some of your ${player.world.definitions.get(ItemDef::class.java, finished[chargeCount-1]).name}.")
            player.animate(829)
            player.inventory.add(Items.VIAL)
            player.message("You have finished your potion.")
        } else if(chargeCount != 0 && player.inventory.remove(finished[chargeCount-1], beginSlot = player.getInteractingItemSlot()).hasSucceeded()) {
            player.message("You drink some of your ${player.world.definitions.get(ItemDef::class.java, finished[chargeCount-1]).name}")
            player.animate(829)
            player.inventory.add(finished[chargeCount-2], beginSlot = player.getInteractingItemSlot())
            player.message("You have ${chargeCount-1} doses of potion left.")
        } else { // not valid charge config
            // could only happen if [Pot.consume()] was called from an invalid pot registration
            KLogging().logger.error("invalid pot registration for ${player.getInteractingItem().id}")
        }
    }
}

enum class Pots(val pot: Pot) {
    ATTACK(Pot(Items.GUAM_POTION_UNF, Items.EYE_OF_NEWT, intArrayOf(Items.ATTACK_POTION1, Items.ATTACK_POTION2, Items.ATTACK_POTION3, Items.ATTACK_POTION4), 3, 25.0)),
    ANTIPOISON(Pot(Items.MARRENTILL_POTION_UNF, Items.UNICORN_HORN_DUST, intArrayOf(Items.ANTIPOISON1, Items.ANTIPOISON2, Items.ANTIPOISON3, Items.ANTIPOISON4), 5, 37.5)),
    RELICYMS_BALM(Pot(Items.UNFINISHED_POTION_4840, Items.SNAKE_WEED, intArrayOf(Items.RELICYMS_BALM1, Items.RELICYMS_BALM2, Items.RELICYMS_BALM3, Items.RELICYMS_BALM4), 8, 40.0)),
    STRENGTH(Pot(Items.TARROMIN_POTION_UNF, Items.LIMPWURT_ROOT, intArrayOf(Items.STRENGTH_POTION1, Items.STRENGTH_POTION2, Items.STRENGTH_POTION3, Items.STRENGTH_POTION4), 12, 50.0)),
    SERUM_207(Pot(Items.TARROMIN_POTION_UNF, Items.ASHES, intArrayOf(Items.SERUM_207_1, Items.SERUM_207_2, Items.SERUM_207_3, Items.SERUM_207_4), 15, 50.0)),
    COMPOST(Pot(Items.HARRALANDER_POTION_UNF, Items.VOLCANIC_ASH, intArrayOf(Items.COMPOST_POTION1, Items.COMPOST_POTION2, Items.COMPOST_POTION3, Items.COMPOST_POTION4), 22, 60.0)),
    RESTORE(Pot(Items.HARRALANDER_POTION_UNF, Items.RED_SPIDERS_EGGS, intArrayOf(Items.RESTORE_POTION1, Items.RESTORE_POTION2, Items.RESTORE_POTION3, Items.RESTORE_POTION4), 22, 62.5)),


}










