package gg.rsmod.plugins.content.skills.smithing

import com.google.common.collect.ImmutableSet
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
val standardFurnaces = ImmutableSet.of(
        Objs.FURNACE_24009
)!!

/**
 * Handles the smelting of the standard bars
 *
 * @param player    The player instance
 * @param item      The item the player is trying to smelt
 * @param amount    The number of bars the player is trying to smelt
 */
fun standardSmeltItem(player: Player, item: Int, amount: Int) {
    val def = barDefs[item] ?: return
    player.queue { smelting.smelt(this, def, amount) }
}

/**
 * Handle smelting at any 'standard' furnace
 */
standardFurnaces.forEach { furnace ->
    on_obj_option(obj = furnace, option = "smelt") {
        player.queue { produceItemBox(*standardBarIds, title = "What would you like to smelt?", logic = ::standardSmeltItem) }
    }
}
