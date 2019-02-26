package gg.rsmod.plugins.content.objs.crates

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.player

val CRATES = ImmutableSet.of(Objs.CRATE_354, Objs.CRATE_355, Objs.CRATE_356, Objs.CRATE_357,
        Objs.CRATE_358, Objs.CRATE_366, Objs.CRATE_1990, Objs.CRATE_1999, Objs.CRATE_2064)

CRATES.forEach { crate ->
    on_obj_option(obj = crate, option = "search") {
        it.player().message("You search the crate but find nothing.")
    }
}