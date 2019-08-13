package gg.rsmod.plugins.content.objs.climbable

/**Lumby staircase floor*/
on_obj_option(Objs.STAIRCASE_16671, option = "climb-up") {
    player.moveTo(3205, 3209, 1)
}

/**Lumby staircase middle*/
private val STAIRCASE2 = 16672
on_obj_option(obj = STAIRCASE2, option = "climb") {
    player.queue {
        when (options("Climb up the stairs.", "Climb down the stairs.", title = "Climb up or down the stairs?")) {
            1 -> player.moveTo(3205, 3209, 2)
            2 -> player.moveTo(3206, 3208, 0)
            }
        }
    }
on_obj_option(obj = STAIRCASE2, option = "climb-up") {
    player.moveTo(3205, 3209, 2)
}
on_obj_option(obj = STAIRCASE2, option = "climb-down") {
    player.moveTo(3206, 3208, 0)
}

/**Lumby staircase top*/
on_obj_option(Objs.STAIRCASE_16673, option = "climb-down") {
    player.moveTo(3206, 3208, 1)
}

/**Dwarven mine*/
on_obj_option(Objs.STAIRCASE_16664, option = "climb-down") {
    if (player.tile.x == 3061 && player.tile.z == 3376) {
        player.moveTo(3058, 9776)
    } else if (player.tile.x == 3061 && player.tile.z == 3377) {
        player.moveTo(3058, 9777)
    }
}
on_obj_option(Objs.STAIRCASE_23969, option = "climb-up") {
    if (player.tile.x == 3058 && player.tile.z == 9776) {
        player.moveTo(3061, 3376)
    } else if (player.tile.x == 3058 && player.tile.z == 9777) {
        player.moveTo(3061, 3377)
    }
}

