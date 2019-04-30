package gg.rsmod.plugins.content.objs.crates

private val CRATES = setOf(
        Objs.CRATE_354, Objs.CRATE_355, Objs.CRATE_356, Objs.CRATE_357,
        Objs.CRATE_358, Objs.CRATE_366, Objs.CRATE_1990, Objs.CRATE_1999, Objs.CRATE_2064)

CRATES.forEach { crate ->
    on_obj_option(obj = crate, option = "search") {
        player.message("You search the crate but find nothing.")
    }
}