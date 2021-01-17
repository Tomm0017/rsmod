package gg.rsmod.plugins.content.mechanics.water

/**
 * Handle the filling of each [WaterContainers]' [Item] for each of the [WaterSources]
 */
WaterSources.values().forEach { source ->
    source.waterObjIds.forEach { obj ->
        WaterContainers.values().forEach {
            on_item_on_obj(obj = obj, item = it.container.unfilled) {
                val message = if(it.container.unfilled.getItemName(world.definitions).contains("Cup"))
                    "You fill the cup."
                else
                    source.message.replaceItemName(it.container.unfilled, world.definitions)
                it.container.fill(player, message)
            }
        }
    }
}

/**
 * Handle the emptying of each [WaterContainers]' [Item] except for Watering can
 * and Waterskin as they DO NOT have "Empty" option, only "Drop" or "Use".
 */
WaterContainers.values().filter { it != WaterContainers.CAN && it != WaterContainers.WATERSKIN }.forEach {
    on_item_option(item = it.container.filled, option = "Empty") {
        it.container.empty(player)
    }
}

WaterContainers.values().forEach {
    /**
     * Using [WaterContainer] on another one does nothing,
     * you cannot transfer water around like pots, you must fill them.
     */
    on_item_on_item(it.container.filled, it.container.filled) {
        player.nothingMessage()
    }
    on_item_on_item(it.container.unfilled, it.container.filled) {
        player.nothingMessage()
    }

    /**
     * Toy sink item!
     */
    on_item_on_item(Items.SINK, it.container.unfilled){
        it.container.fill(player, "The cute sink fills the ${it.container.unfilled.getItemName(world.definitions, lowercase = true)} to the brim.")
    }
    on_item_on_item(Items.SINK, it.container.filled){
        player.message("The ${it.container.unfilled.getItemName(world.definitions, lowercase = true)} cannot hold any more water.")
    }
}

/**
 * sexy little drop hack creates a toy sink object one tile north
 * of player and queues it for removal after 300 cycles (~3minutes)
 * also prevents from dropping item from inventory, but could be done here
 */
can_drop_item(Items.SINK) {
    val obj = DynamicObject(Objs.TOY_SINK, 10, 3, player.tile.transform(0,1))
    world.spawn(obj)
    player.world.queue {
        wait(300)
        world.remove(obj)
    }
    false
}

/**
 * hot water is apparently only created in bowls lol, registering bowls to heat
 * here would require knowing all the fire sources so we'll ignore lack of ability
 * to make for now; this is mostly used for testing Guthix rest teas without heat plugins.
 */
on_item_on_item(Items.BOWL_OF_HOT_WATER, Items.EMPTY_CUP){
    if(player.comboItemReplace(oldItem = Items.EMPTY_CUP, newItem = Items.CUP_OF_HOT_WATER,
            otherOld = Items.BOWL_OF_HOT_WATER, otherNew = Items.BOWL, slotAware = true))
        player.message("You pour the hot water into the tea cup.")
}
