package gg.rsmod.plugins.content.objs.underwall

on_obj_option(16529, "climb-into") {
    player.climbIntoUnderwallwest()
}

on_obj_option(16530, "climb-into") {
    player.climbIntoUnderwalleast()
}

fun Player.climbIntoUnderwallwest() {
    queue {
        lock()
        animate(2589)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3138, z = 3516, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.EAST.angle))
        wait(1)
        animate(2590)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3139, z = 3515, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.EAST.angle))
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3140, z = 3514, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.EAST.angle))
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3141, z = 3513, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.EAST.angle))
        animate(2591)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3142, z = 3513, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.EAST.angle))
        animate(-1)
        player.unlock()
    }
}

fun Player.climbIntoUnderwalleast() {
    queue {
        lock()
        animate(2589)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3141, z = 3513, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.WEST.angle))

        wait(1)
        animate(2590)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3140, z = 3514, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.WEST.angle))
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3139, z = 3515, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.WEST.angle))
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3138, z = 3516, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.WEST.angle))
        animate(2591)
        forceMove(this, ForcedMovement.of(player.tile, Tile(x = 3137, z = 3516, height = 0), clientDuration1 = 33, clientDuration2 = 60, directionAngle = Direction.WEST.angle))
        animate(-1)
        player.unlock()
    }
}