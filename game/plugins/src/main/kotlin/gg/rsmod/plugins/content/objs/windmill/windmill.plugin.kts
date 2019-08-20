package gg.rsmod.plugins.content.objs.windmill

/**
 * Thanks to Desetude for the working windmill
 * */

val GRAIN_IN_HOPPER_ATTRIBUTE = AttributeKey<Unit>(persistenceKey = "grain_in_hopper")
val FLOUR_COLLECTABLE_VARBIT = 5325
val FLOUR_AMOUNT_VARBIT = 4920

on_item_option(Items.POT_OF_FLOUR, "empty") {
    player.inventory.remove(Items.POT_OF_FLOUR)
    player.inventory.add(Items.POT)
}

on_item_on_obj(Objs.HOPPER_24961, Items.GRAIN) {
    if (!player.attr.has(GRAIN_IN_HOPPER_ATTRIBUTE)) {
        player.queue {
            player.lock()
            player.animate(3572)
            wait(2)
            player.unlock()
            player.inventory.remove(Items.GRAIN)
            player.attr.put(GRAIN_IN_HOPPER_ATTRIBUTE, Unit)
            player.filterableMessage("You put the grain in the hopper. You should now pull the lever nearby to operate the hopper.")
        }
    } else {
        player.message("There is already grain in the hopper.")
    }
}

on_obj_option(Objs.HOPPER_CONTROLS_24964, "operate") {
    val obj = player.getInteractingGameObj()

    player.queue {
        player.lock()
        player.animate(3571)
        wait(2)

        world.queue {
            world.remove(obj)
            world.spawn(DynamicObject(obj, Objs.HOPPER_CONTROLS_24967))
            wait(2)
            world.remove(DynamicObject(obj, Objs.HOPPER_CONTROLS_24967))
            world.spawn(DynamicObject(obj))
        }

        player.unlock()

        if (!player.attr.has(GRAIN_IN_HOPPER_ATTRIBUTE)) {
            player.message("You operate the empty hopper. Nothing interesting happens.")
            return@queue
        }

        val amount = player.getVarbit(FLOUR_AMOUNT_VARBIT)
        if (amount == 0) {
            player.setVarbit(FLOUR_COLLECTABLE_VARBIT, 1)
        }

        player.setVarbit(FLOUR_AMOUNT_VARBIT, amount + 1)
        player.attr.remove(GRAIN_IN_HOPPER_ATTRIBUTE)
        player.filterableMessage("You operate the hopper. The grain slides down the chute.")
    }
}

on_obj_option(Objs.FLOUR_BIN, 1) {
    emptyFlour(player)
}

on_item_on_obj(obj = Objs.FLOUR_BIN, item = Items.POT) {
    emptyFlour(player)
}

fun emptyFlour(player: Player) {
    if (!player.inventory.contains(Items.POT)) {
        player.message("You need an empty pot to hold the flour in.")
        return
    }

    var amount = player.getVarbit(FLOUR_AMOUNT_VARBIT)
    if (amount == 0) {
        //What happened?
        return
    }

    amount--

    player.inventory.remove(Items.POT)
    player.inventory.add(Items.POT_OF_FLOUR)
    if (amount == 0) {
        player.setVarbit(FLOUR_COLLECTABLE_VARBIT, 0)
    } else {
        player.filterableMessage("You fill a pot with flour from the bin.")
    }

    player.setVarbit(FLOUR_AMOUNT_VARBIT, amount)
}