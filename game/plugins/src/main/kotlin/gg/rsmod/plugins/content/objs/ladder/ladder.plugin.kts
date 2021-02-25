package gg.rsmod.plugins.content.objs.ladder

on_obj_option(19044, 1) {
    player.climbLadder(Direction.NORTH_WEST)
}

fun Player.climbLadder(direction: Direction) {
    queue {
        player.animate(id = 828, delay = 15)
        forceMove(this, ForcedMovement.of(player.tile, player.tile.step(direction, 1), 10, 60, direction.angle))
    }
}