package gg.rsmod.plugins.content.skills.fletching

import gg.rsmod.plugins.content.skills.fletching.action.ChiselAction
import gg.rsmod.plugins.content.skills.fletching.data.Chiseled

// ===========================================================================
// Whittling Logs
/**
 * The map of Chiseled item ids to their definition
 */
val chiseledDefs = Chiseled.chiseledDefinitions

/**
 * The Chiseling action instance
 */
val chiselAction = ChiselAction(world.definitions)

/**
 * Handles using a chisel on a gem or kebbit
 */
chiseledDefs.values.forEach { chiseled ->
    on_item_on_item(item1 = Items.CHISEL, item2 = chiseled.unchiseled) { makeChiseled(player, chiseled.id) }
}

/**
 * Opens the prompt to allow the player to select the number of items to chisel
 *
 * @param player    The player instance
 */
fun makeChiseled(player: Player, chiseled: Int) {
    val chiseledDef = chiseledDefs[chiseled] ?: return
    val maxChiseled = player.inventory.getItemCount(chiseledDef.unchiseled)
    when (maxChiseled) {
        0 -> return
        1 -> chisel(player, chiseled, 1)
        else -> player.queue { produceItemBox(chiseledDef.id, type = 2,  maxItems = maxChiseled, logic = ::chisel) }
    }
}

/**
 * Handles the chiseling of an item into the chiseled item id
 *
 * @param player    The player instance
 * @param item      The item the player is trying to whittle the log into
 * @param amount    The number of items the player is trying to smelt
 */
fun chisel(player: Player, item: Int, amount: Int) {
    val chiseledDef = chiseledDefs[item] ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue { chiselAction.chisel(this, chiseledDef, amount) }
}