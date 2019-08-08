package gg.rsmod.plugins.content.mechanics.cluescrolls

val INTERFACE_ID = 203
var cluestep = 0

arrayOf(Items.CLUE_BOTTLE_BEGINNER, Items.CLUE_GEODE_BEGINNER, Items.CLUE_NEST_BEGINNER).forEach { beginnerclue ->
    on_item_option(beginnerclue, option = "open") {
        player.inventory.remove(beginnerclue)
        player.inventory.add(Items.CLUE_SCROLL_BEGINNER)
    }
}
arrayOf(Items.CLUE_BOTTLE_EASY, Items.CLUE_GEODE_EASY, Items.CLUE_NEST_EASY).forEach { easyclue ->
    on_item_option(easyclue, option = "open") {
        player.inventory.remove(easyclue)
        player.inventory.add(Items.CLUE_SCROLL_EASY)/**2677*/
    }
}
arrayOf(Items.CLUE_BOTTLE_MEDIUM, Items.CLUE_GEODE_MEDIUM, Items.CLUE_NEST_MEDIUM).forEach { mediumclue ->
    on_item_option(mediumclue, option = "open") {
        player.inventory.remove(mediumclue)
        player.inventory.add(Items.CLUE_SCROLL_MEDIUM)/**2801*/
    }
}
arrayOf(Items.CLUE_BOTTLE_HARD, Items.CLUE_GEODE_HARD, Items.CLUE_NEST_HARD).forEach { hardclue ->
    on_item_option(hardclue, option = "open") {
        player.inventory.remove(hardclue)
        player.inventory.add(Items.CLUE_SCROLL_HARD)/**2722*/
    }
}
arrayOf(Items.CLUE_BOTTLE_ELITE, Items.CLUE_GEODE_ELITE, Items.CLUE_NEST_ELITE).forEach { eliteclue ->
    on_item_option(eliteclue, option = "open") {
        player.inventory.remove(eliteclue)
        player.inventory.add(Items.CLUE_SCROLL_ELITE)/**12073*/
    }
}


on_interface_open(INTERFACE_ID) {
    when (world.random(3)) {
        1 -> player.setComponentText(interfaceId = INTERFACE_ID, component = 2, text = "TEXT")
        2 -> player.setComponentText(interfaceId = INTERFACE_ID, component = 2, text = "TEXT2")
        3 -> player.setComponentText(interfaceId = INTERFACE_ID, component = 2, text = "TEXT3")
    }
}

on_item_option(item = 23182, option = "read") {
    when (world.random(3)) {
        1 -> player.openInterface(203, InterfaceDestination.MAIN_SCREEN)
        2 -> player.openInterface(351, InterfaceDestination.MAIN_SCREEN)
        3 -> {

        }
    }
}
            /*cluestep++
            player.openInterface(356, InterfaceDestination.MAIN_SCREEN)
            on_item_option(item = 952, option = "dig") {
                player.animate(830)
                /*if (player.tile.x == 3110 && player.tile.z == 3152 && player.inventory.contains(Items.CLUE_SCROLL_BEGINNER) && cluestep >= 1) {
                    player.queue {
                        player.inventory.add(Items.REWARD_CASKET_BEGINNER)/**23245*/
                        itemMessageBox("You've obtained a casket!", item = 23245, amountOrZoom = 400)
                        cluestep--
                        //needs reset for the main interface needed to clear clue helper
