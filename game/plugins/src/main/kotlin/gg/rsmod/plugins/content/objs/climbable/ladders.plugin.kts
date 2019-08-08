package gg.rsmod.plugins.content.objs.climbable

private val LADDER = 12964
private val LADDER2 = 12965
private val LADDER3 = 12966

/**Lumby staircase floor*/
on_obj_option(obj = LADDER, option = "climb-up") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}

/**Lumby staircase middle*/
on_obj_option(obj = LADDER2, option = "climb") {
    player.queue {
        when (options("Climb Up.", "Climb Down.", title = "Climb up or down the ladder?")) {
            1 -> {
                player.animate(828)
                player.moveTo(player.tile.x, player.tile.z, 2)
            }
            2 -> {
                player.animate(828)
                player.moveTo(player.tile.x, player.tile.z, 0)
            }
        }
    }
}
    on_obj_option(obj = LADDER2, option = "climb-up") {
        player.animate(828)
        player.moveTo(player.tile.x, player.tile.z, 2)
    }
    on_obj_option(obj = LADDER2, option = "climb-down") {
        player.animate(828)
        player.moveTo(player.tile.x, player.tile.z, 0)
    }

on_obj_option(obj = LADDER3, option = "climb-down") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}