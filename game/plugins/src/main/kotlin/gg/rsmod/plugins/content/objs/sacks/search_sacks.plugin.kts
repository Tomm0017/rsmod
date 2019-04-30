package gg.rsmod.plugins.content.objs.sacks

private val SACKS = setOf(Objs.SACKS_365)

SACKS.forEach { sack ->
    on_obj_option(obj = sack, option = "search") {
        player.message("There's nothing interesting in these sacks.")
    }
}