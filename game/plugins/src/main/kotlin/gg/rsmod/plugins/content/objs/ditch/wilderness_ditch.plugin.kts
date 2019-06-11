package gg.rsmod.plugins.content.objs.ditch

on_obj_option(23271, "cross") {
    val ditch = player.getInteractingGameObj()
    val sideCross = player.tile.z == ditch.tile.z

    val endTile: Tile
    val directionAngle: Int

    if (sideCross) {
        val westOfDitch = player.tile.x < ditch.tile.x

        if (westOfDitch) {
            endTile = ditch.tile.step(Direction.EAST, 2)
            directionAngle = Direction.EAST.angle
        } else {
            endTile = ditch.tile.step(Direction.WEST, 1)
            directionAngle = Direction.WEST.angle
        }
    } else {
        val southOfDitch = player.tile.z < ditch.tile.z

        if (southOfDitch) {
            endTile = ditch.tile.step(Direction.NORTH, 2)
            directionAngle = Direction.NORTH.angle
        } else {
            endTile = ditch.tile.step(Direction.SOUTH, 1)
            directionAngle = Direction.SOUTH.angle
        }
    }

    val movement = ForcedMovement.of(player.tile, endTile, clientDuration1 = 33, clientDuration2 = 60, directionAngle = directionAngle)
    player.crossDitch(movement)
}

fun Player.crossDitch(movement: ForcedMovement) {
    queue {
        playSound(2452)
        animate(6132)
        forceMove(this, movement)
    }
}