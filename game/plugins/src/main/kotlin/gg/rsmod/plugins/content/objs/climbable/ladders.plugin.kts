package gg.rsmod.plugins.content.objs.climbable

fun faceWest(player: Player) {
    val direction : Direction = Direction.WEST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
}
fun faceEast(player: Player) {
    val direction : Direction = Direction.EAST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
}
fun moveTo (player: Player, int: Int) {
    val obj = player.getInteractingGameObj()
    player.queue {
        player.walkTo(this, obj.tile)
        wait(3)
        player.animate(int)
        wait(2)
    }
}

/**Windmill bottom floor*/
on_obj_option(Objs.LADDER_12964, option = "climb-up") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}

/**Windmill middle floor*/
private val LADDER2 = 12965
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

/**Windmill top floor*/
on_obj_option(Objs.LADDER_12966, option = "climb-down") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}

/**Wood Cutting Guild ladders*/
on_obj_option(obj = Objs.ROPE_LADDER_28857, option = "climb-up") {
    player.queue {
        moveTo(player, 828)
        wait(2)
        if (player.tile.x == 1566 && player.tile.z == 3483 || player.tile.x == 1566 && player.tile.z == 3493) {
            faceEast(player)
            player.moveTo(player.tile.x + 2, player.tile.z, 1)
        } else
            faceWest(player)
            player.moveTo(player.tile.x - 1, player.tile.z, 1)
    }
}
on_obj_option(obj = Objs.ROPE_LADDER_28858, option = "climb-down") {
    val obj = player.getInteractingGameObj()
    player.queue {
        moveTo(player, 828)
        wait(2)
        player.faceTile(obj.tile)
        if (player.tile.x == 1567 && player.tile.z == 3483 || player.tile.x == 1567 && player.tile.z == 3493) {
            player.moveTo(player.tile.x - 1, player.tile.z, 0)
        } else player.moveTo(player.tile.x + 1, player.tile.z, 0)
    }
}

