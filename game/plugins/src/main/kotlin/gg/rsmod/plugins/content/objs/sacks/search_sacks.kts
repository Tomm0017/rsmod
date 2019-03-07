package gg.rsmod.plugins.content.objs.sacks

import com.google.common.collect.ImmutableSet

val SACKS = ImmutableSet.of(Objs.SACKS_365)

SACKS.forEach { sack ->
    on_obj_option(obj = sack, option = "search") {
        player.message("There's nothing interesting in these sacks.")
    }
}