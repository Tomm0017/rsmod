package gg.rsmod.plugins.content.skills.herblore

import gg.rsmod.plugins.content.skills.herblore.pots.Pots
import gg.rsmod.game.model.priv.Privilege

on_command("autoclean", Privilege.ADMIN_POWER){
    Herbs.AUTO_CLEAN = !Herbs.AUTO_CLEAN
    player.message("Herb auto-cleaning ${if(Herbs.AUTO_CLEAN) "enabled" else "disabled"}.")
}

/**
 * Registers each of the dirty [Herbs] for cleaning option
 */
Herbs.values().forEach {
    on_item_option(item = it.herb.dirty, option = "Clean") {
        it.herb.clean(player)
    }
}

on_button(270, 14) {
    player.closeInterface(270)
    player.message("making stuffs...")
}

var hCleanMapped = 0
Herbs.values().forEach { h ->
    on_item_on_item(h.herb.clean, Items.VIAL_OF_WATER) {
        val numClean = player.inventory.getItemCount(h.herb.clean)
        val numWaterVials = player.inventory.getItemCount(Items.VIAL_OF_WATER)
        val maxCount = Math.min(numClean, numWaterVials)

        player.produceItemBoxMessage(h.herb.unfinished, max = maxCount){
            h.herb.apoth(player)
        }
    }

    /**
     * herb on herb actions do nothing.
     */
    Herbs.values().filter { it.ordinal >= hCleanMapped++ }.forEach { h2 ->
        on_item_on_item(h.herb.clean, h2.herb.clean){
            player.nothingMessage()
        }
        on_item_on_item(h.herb.clean, h2.herb.dirty){
            player.nothingMessage()
        }
        on_item_on_item(h.herb.dirty, h2.herb.dirty){
            player.nothingMessage()
        }
    }

    /**
     * herb on finished potions do nothing
     */
    Pots.values().forEach { p ->
        p.pot.finished.forEach { f ->
            on_item_on_item(h.herb.dirty, f) {
                player.nothingMessage()
            }
            on_item_on_item(h.herb.clean, f) {
                player.nothingMessage()
            }
        }
    }
}