package gg.rsmod.plugins.content.skills.smithing

import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.plugins.content.skills.smithing.action.SmithingAction
import gg.rsmod.plugins.content.skills.smithing.data.Bar
import gg.rsmod.plugins.content.skills.smithing.data.SmithingData
import gg.rsmod.plugins.content.skills.smithing.data.SmithingMetaData
import gg.rsmod.plugins.content.skills.smithing.data.typeForChild

/**
 * The smithing meta data instance
 */
val smithingData = SmithingData(world.definitions)

/**
 * The map of bar item ids to their definitions
 */
val barDefs = Bar.barDefinitions.filter { smithingData.barIndices.contains(it.key) }

/**
 * The set of bar item ids
 */
val barIds = barDefs.keys

/**
 * The smithing interface child components
 */
val smithingInterfaceComponents = IntRange(2, 30)

/**
 * The smithing interface id
 */
val smithingInterface = 312

/**
 * The varbit containing the current bar the player is smithing
 */
val smithingCurrentBarVarbit = 3216

/**
 * A map used as a cache. This cache maps bar to a map containing
 * the possible child ids, and the meta data for the child id
 */
val itemCache : HashMap<Bar, HashMap<Int, SmithingMetaData?>> = HashMap()

/**
 * The set of 'standard' anvils in the game
 */
private val standardAnvils = setOf(
    Objs.ANVIL_2097
)

/**
 * For each anvil, bind the usage of a hammer (it provides information for the player about the smithing skill),
 * bind the action of using a bar on the anvil (to explicity specify what type of metal the player would like to forge), and bind
 * the "smith" option on an anvil (will prompt to smith the first bar the player has the level for)
 */
standardAnvils.forEach { anvil ->

    // Bind the usage of a hammer on an anvil
    on_item_on_obj(obj = anvil, item = Items.HAMMER) {
        player.message("To smith a metal bar, you can click on the anvil while you have the bar in your inventory.")
    }

    // Bind the usage of a bar on anvil
    barIds.forEach { bar ->
        on_item_on_obj(obj = anvil, item = bar) {

            // The definition of the bar
            val def = barDefs[bar]

            // Check if the player can smith the item
            player.queue(TaskPriority.WEAK) {
                // The player can't smith the item, do nothing
                if (!SmithingAction.canSmithBar(this, def))
                    return@queue

                // Open the smithing interface
                openSmithingInterface(player, def!!)
            }
        }
    }

    // Bind the "smith" action on an anvil
    on_obj_option(obj = anvil, option = "smith") {

        // The bar to smith
        val bar = getBar(player)

        // Check if the player can smith a bar
        player.queue(TaskPriority.WEAK) {

            // If the player can't smith the bar, do nothing
            if (!SmithingAction.canSmithBar(this, bar))
                return@queue

            // Queue the smithing interface
            openSmithingInterface(player, bar!!)
        }
    }
}

/**
 * Gets an available bar to smith for a player
 *
 * @param player    The player instance
 * @return          The bar to smith
 */
fun getBar(player: Player) : Bar? {

    // The player's inventory
    val inventory = player.inventory

    // The last bar that was smithed
    val lastBar = smithingData.smithableBarsEnum.getInt(player.getVarbit(smithingCurrentBarVarbit))

    return if (barDefs.containsKey(lastBar) && inventory.contains(lastBar)) {
        barDefs[lastBar]
    } else {
        inventory.filter { barDefs.containsKey(it?.id) }.map { barDefs[it?.id]!! }.firstOrNull { player.getSkills().getCurrentLevel(Skills.SMITHING) >= it.level }
    }
}

/**
 * Opens the smithing interface and waits for a selection to be made
 *
 * @param player    The player instance
 * @param bar       The bar definition
 */
fun openSmithingInterface(player: Player, bar: Bar) {
    player.setVarbit(smithingCurrentBarVarbit, smithingData.barIndices.getValue(bar.id))
    player.openInterface(interfaceId = smithingInterface, dest = InterfaceDestination.MAIN_SCREEN)
}

/**
 * Listen on the child components for the smithing interface
 */
smithingInterfaceComponents.forEach { child ->
    on_button(interfaceId = smithingInterface, component = child) {

        // The bar being smithed
        val barId = smithingData.smithableBarsEnum.getInt(player.getVarbit(smithingCurrentBarVarbit))
        val bar = barDefs[barId] ?: return@on_button

        // If the cache contains an entry for this child
        val barItems = itemCache.getOrPut(bar) { HashMap() }

        // The item in the cache for the specified child
        val item = barItems.getOrPut(child) {

            val barMetaItems = smithingData.barItemData[bar]
            val type = typeForChild(child, bar)
            type?.let { barMetaItems?.firstOrNull { it.name.endsWith(type) } }
        }

        // If the item is valid, queue the smithing action
        item?.let {

            // Queue the smithing action
            player.queue(TaskPriority.STRONG) {

                // The number of items to smith
                val amount = when (player.attr[INTERACTING_OPT_ATTR]) {
                    1 -> 1
                    2 -> 5
                    3 -> 10
                    4 -> inputInt("Enter amount:")
                    5 -> player.inventory.capacity
                    else -> {
                        world.sendExamine(player, item.id, ExamineEntityType.ITEM)
                        return@queue
                    }
                }

                // Close the smithing interface
                player.closeInterface(smithingInterface)

                // Process the smithing action
                SmithingAction.smith(this, item, amount)
            }
        }
    }

}