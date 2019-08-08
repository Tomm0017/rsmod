package gg.rsmod.plugins.content.skills.fletching

import gg.rsmod.plugins.content.skills.fletching.data.Tipped
import gg.rsmod.plugins.content.skills.fletching.action.TipAction

// ===========================================================================
// Tipping Ammunition
/**
 * The map of tipped ids to their definition
 */
val tippedDefs = Tipped.tippedDefinitions

/**
 * The item tipping action instance
 */
val tippedAction = TipAction(world.definitions)

/**
 * Handles using a untipped ammunition on a tip
 */
tippedDefs.values.forEach { tipped ->
    on_item_on_item(item1 = tipped.base, item2 = tipped.tip) { makeTipped(player, tipped.id) }
}

/**
 * Opens the prompt to get the quantity to tip unless they can only tip a single set or less
 *
 * @param player    The player instance
 * @param tipped    The tipped item id the player is trying to make
 */
fun makeTipped(player: Player, tipped: Int) {
    val tippedDef = tippedDefs[tipped] ?: return
    val maxTipped = Math.ceil(Math.min(player.inventory.getItemCount(tippedDef.base), player.inventory.getItemCount(tippedDef.tip)) / tippedDef.setAmount.toDouble()).toInt()
    when (maxTipped) {
        0 -> return
        1 -> tip(player, tipped, 1)
        else -> player.queue { produceItemBox(tippedDef.id, type = 3,  maxItems = maxTipped, logic = ::tip) }
    }
}

/**
 * Handles the tipping of the selected tipped item
 *
 * @param player    The player instance
 * @param item      The tipped item id the player is trying to make
 * @param amount    The number of items the player is trying to make
 */

fun tip(player: Player, item: Int, amount: Int) {
    val tippedDef = tippedDefs[item] ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue { tippedAction.tip(this, tippedDef, amount) }
}