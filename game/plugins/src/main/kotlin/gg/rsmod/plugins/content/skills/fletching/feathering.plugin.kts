package gg.rsmod.plugins.content.skills.fletching

import gg.rsmod.plugins.content.skills.fletching.data.Feathered
import gg.rsmod.plugins.content.skills.fletching.action.FeatherAction
import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.OTHER_ITEM_ID_ATTR


// ===========================================================================
// Feathering Darts/Bolts
/**
 * The map of Feathered ids to their definition
 */
val featheredDefs = Feathered.featheredDefinitions

/**
 * A list of all possible feathers to be used
 */
val feathers = Feathered.feathers

/**
 * The feathering action instance
 */
val featherAction = FeatherAction(world.definitions)

/**
 * Handles using a unfinished item on a feather
 */
featheredDefs.values.forEach { feathered ->
    feathers.forEach { feather ->
        if(feathered.id == Items.HEADLESS_ARROW || feathered.id == Items.FLIGHTED_OGRE_ARROW)
            on_item_on_item(item1 = feathered.unfeathered, item2 = feather) { featherShaft(player, feathered.id) }
        else
            on_item_on_item(item1 = feathered.unfeathered, item2 = feather) { feather(player, feathered.id) }
    }
}

/**
 * This is the one exception to the feathered items which sends the item box
 *
 * @param player    The player instance
 * @param feathered The feathered item the player is trying to make
 */
fun featherShaft(player: Player, feathered: Int){
    val feather = if(feathers.contains(player.attr[INTERACTING_ITEM_ID])){
        player.attr[INTERACTING_ITEM_ID]
    } else if(feathers.contains(player.attr[OTHER_ITEM_ID_ATTR])){
        player.attr[OTHER_ITEM_ID_ATTR]
    } else {
        null
    }
    feather ?: return

    val featheredDef = featheredDefs[feathered] ?: return
    val maxFeatherableSets = Math.ceil(Math.min(player.inventory.getItemCount(featheredDef.unfeathered), (player.inventory.getItemCount(feather) / featheredDef.feathersNeeded)) / featheredDef.amount.toDouble()).toInt()
    when (maxFeatherableSets) {
        0 -> return
        1 -> feather(player, featheredDef.id)
        else -> player.queue { produceItemBox(feathered, type = 3,  maxItems = maxFeatherableSets, logic = ::feather) }
    }
}

/**
 * Queues one more set of feathering for the user (unless headless arrows)
 *
 * @param player    The player instance
 * @param feathered The feathered item the player is trying to make
 * @param amount    The amount the player is trying to make ( defaulted to 1 )
 */
fun feather(player: Player, feathered: Int, amount: Int = 1) {
    val feather = if(feathers.contains(player.attr[INTERACTING_ITEM_ID])){
        player.attr[INTERACTING_ITEM_ID]
    } else if(feathers.contains(player.attr[OTHER_ITEM_ID_ATTR])){
        player.attr[OTHER_ITEM_ID_ATTR]
    } else {
        null
    }
    feather ?: return

    val featheredDef = featheredDefs[feathered] ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue{ featherAction.feather(this, featheredDef, feather, amount) }
}