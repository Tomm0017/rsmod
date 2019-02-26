package gg.rsmod.plugins.content.objs.sacks

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.player

val SACKS = ImmutableSet.of(Objs.SACKS_365)

SACKS.forEach { sack ->
    on_obj_option(obj = sack, option = "search") {
        it.player().message("There's nothing interesting in these sacks.")
    }
}