package gg.rsmod.plugins.content.objs.ditch

on_obj_option(23271, "cross") {
    val ditch = player.getInteractingGameObj()
    val southOfDitch = player.tile.z < ditch.tile.z

    val movement = if (southOfDitch) {
        ForcedMovement.of(player.tile, ditch.tile.step(Direction.NORTH, 2), clientDuration1 = 33,
                clientDuration2 = 60, directionAngle = Direction.NORTH.angle)
    } else {
        ForcedMovement.of(player.tile, ditch.tile.step(Direction.SOUTH, 1), clientDuration1 = 33,
                clientDuration2 = 60, directionAngle = Direction.SOUTH.angle)
    }

    player.crossDitch(movement)
}

fun Player.crossDitch(movement: ForcedMovement) {
    queue {
        playSound(2452)
        forceMove(this, movement, 6132)
    }
}