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