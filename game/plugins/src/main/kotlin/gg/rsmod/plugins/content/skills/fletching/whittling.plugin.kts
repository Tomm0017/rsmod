package gg.rsmod.plugins.content.skills.fletching

import gg.rsmod.plugins.content.skills.fletching.data.Log
import gg.rsmod.plugins.content.skills.fletching.action.WhittlingAction
import gg.rsmod.game.model.attr.OTHER_ITEM_ID_ATTR
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID

// ===========================================================================
// Whittling Logs
/**
 * The map of Log ids to their definition
 */
val logDefs = Log.logDefinitions

/**
 * The whittling action instance
 */
val whittleAction = WhittlingAction(world.definitions)

/**
 * Handles using a knife on logs
 */
logDefs.keys.forEach { log ->
    on_item_on_item(item1 = Items.KNIFE, item2 = log) { cutLog(player, log) }
}

/**
 * Opens the prompt to show the log's fletchable items
 *
 * @param player    The player instance
 */
fun cutLog(player: Player, log: Int) {
    val fletchables = logDefs[log]?.values?.map { fletchable -> fletchable.id }?.toIntArray() ?: return
    player.queue { produceItemBox(*fletchables, type = 12, logic = ::whittle) }
}

/**
 * Handles the whittling of the log into the selected item
 *
 * @param player    The player instance
 * @param item      The item the player is trying to whittle the log into
 * @param amount    The number of items the player is trying to smelt
 */

fun whittle(player: Player, item: Int, amount: Int) {
    val log = if(logDefs.containsKey(player.attr[INTERACTING_ITEM_ID])){
        player.attr[INTERACTING_ITEM_ID]
    } else if(logDefs.containsKey(player.attr[OTHER_ITEM_ID_ATTR])){
        player.attr[OTHER_ITEM_ID_ATTR]
    } else {
        null
    }
    log ?: return

    val whittleOption = logDefs[log]?.get(item) ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue { whittleAction.whittle(this, log, whittleOption, amount) }
}