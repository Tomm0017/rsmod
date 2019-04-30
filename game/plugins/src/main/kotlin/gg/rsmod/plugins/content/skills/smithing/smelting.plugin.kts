package gg.rsmod.plugins.content.skills.smithing

import gg.rsmod.plugins.content.skills.smithing.action.SmeltingAction
import gg.rsmod.plugins.content.skills.smithing.data.Bar

/**
 * The [Bar] enum values
 */
val bars = Bar.values

/**
 * The map of [Bar] ids to their definition
 */
val barDefs = Bar.barDefinitions

/**
 * An array of [Bar] item ids that may be smelted at any standard furnace
 */
val standardBarIds = bars.filter { it != Bar.LOVAKITE }.map { bar -> bar.id }.toIntArray()

/**
 * The smelting action instance
 */
val smelting = SmeltingAction(world.definitions)

/**
 * The set of 'standard' furnaces
 */
val standardFurnaces = setOf(
        Objs.FURNACE_24009
)

/**
 * Handles the smelting of the standard bars
 *
 * @param player    The player instance
 * @param item      The item the player is trying to smelt
 * @param amount    The number of bars the player is trying to smelt
 */
fun smeltItem(player: Player, item: Int, amount: Int = 28) {
    val def = barDefs[item] ?: return
    player.queue { smelting.smelt(this, def, amount) }
}

/**
 * Handle smelting at any 'standard' furnace
 */
standardFurnaces.forEach { furnace ->

    on_obj_option(obj = furnace, option = "smelt") { smeltStandard(player) }

    standardBarIds.forEach { on_item_on_obj(obj = furnace, item = it) { smeltStandard(player) } }

    on_item_on_obj(obj = furnace, item = Items.LOVAKITE_ORE) {
        player.queue { messageBox("This furnace is the wrong temperature for lovakite ore.") }
    }
}

/**
 * Opens the proompt to smelt any standard bar
 *
 * @param player    The player instance
 */
fun smeltStandard(player: Player) {
    player.queue { produceItemBox(*standardBarIds, title = "What would you like to smelt?", logic = ::smeltItem) }
}

/**
 * Handles smelting at the Lovakite furnace
 */
on_obj_option(obj = Objs.LOVAKITE_FURNACE, option = "smelt") { smeltItem(player, Items.LOVAKITE_BAR) }
on_item_on_obj(obj = Objs.LOVAKITE_FURNACE, item = Items.LOVAKITE_ORE) { smeltItem(player, Items.LOVAKITE_BAR) }