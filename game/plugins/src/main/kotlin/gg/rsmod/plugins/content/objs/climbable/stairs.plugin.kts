package gg.rsmod.plugins.content.objs.climbable

private val STAIRCASE = 16671
private val STAIRCASE2 = 16672
private val STAIRCASE3 = 16673

/**Lumby staircase floor*/
on_obj_option(obj = STAIRCASE, option = "climb-up") {
    player.moveTo(3205, 3209, 1)
}

/**Lumby staircase middle*/
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
on_obj_option(obj = STAIRCASE3, option = "climb-down") {
    player.moveTo(3206, 3208, 1)
}