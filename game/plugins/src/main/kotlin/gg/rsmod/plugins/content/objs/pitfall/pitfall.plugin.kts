package gg.rsmod.plugins.content.objs.pitfall

import gg.rsmod.plugins.content.combat.getLastHitByAttr

on_obj_option(19227, "trap") {
    val ditch = player.getInteractingGameObj()

    val playerMovement = ForcedMovement.of(player.tile, getEndTile(player.tile, ditch.tile), clientDuration1 = 33, clientDuration2 = 60, directionAngle = getDirectionAngle(player.tile,ditch.tile))
    player.crossDitch(playerMovement)
    if (player.getLastHitByAttr() != null) {
        val lastNpcHitBy = player.getLastHitByAttr()  as Npc
        when (lastNpcHitBy.entityType == EntityType.NPC) { true ->
                if (lastNpcHitBy.tile.getDistance(ditch.tile) <= 4) {
                    player.queue {
                        lastNpcHitBy.flee(lastNpcHitBy, player, ditch.tile)
                }
            }
        }
    }
}

fun Player.crossDitch(movement: ForcedMovement) {
    queue {
        playSound(2452)
        animate(6132)
        forceMove(this, movement)
    }
}

/*
suspend fun jumpover(it: QueueTask, p: Player, n: Npc) {
    val flee = world.percentChance(15.0)

    p.lock()
    p.animate(893) //tease anim
    //p.playSound(761)
    if (flee) {
        flee(n,p)
    }
    it.wait(2)
    p.unlock()
    p.playSound(762)
    n.forceChat("Baa!")

    if (!flee) {
        //p.message("You get some wool.")
        n.queue { turn_to_corpse(this, n) }
    } else {
        // p.message("The sheep manages to get away from you!")
    }
}
*/
fun Npc.flee(n: Npc, p : Player, objTile : Tile) {
    //val npcMovement = ForcedMovement.of(n.tile,  getEndTile(n.tile, objTile), clientDuration1 = 33, clientDuration2 = 60, directionAngle = getDirectionAngle(n.tile,objTile))

    p.message("npc walktoplayer.")
        //n.walkTo(getEndTile(n.tile,p.tile))
        n.walkTo(tile = getEndTile(n.tile, objTile), stepType = MovementQueue.StepType.NORMAL, detectCollision = false)
}

fun getEndTile(tile1 : Tile, tile2 : Tile): Tile {
    val sideCross = tile1.z == tile2.z

    val endTile: Tile

    if (sideCross || tile1.z == tile2.z+1) {
        val westOfDitch = tile1.x < tile2.x

        if (westOfDitch) {
            endTile = tile1.step(Direction.EAST, 3)
        } else {
            endTile = tile1.step(Direction.WEST, 3)
        }
    } else {
        val southOfDitch = tile1.z < tile2.z

        if (southOfDitch) {
            endTile = tile1.step(Direction.NORTH, 3)
        } else {
            endTile = tile1.step(Direction.SOUTH, 3)
        }
    }

    return endTile
}
 fun getDirectionAngle(tile1 : Tile, tile2 : Tile): Int {
    val sideCross = tile1.z == tile2.z

    val directionAngle: Int

    if (sideCross || tile1.z == tile2.z+1) {
        val westOfDitch = tile1.x < tile2.x

        if (westOfDitch) {
            directionAngle = Direction.EAST.angle
        } else {
            directionAngle = Direction.WEST.angle
        }
    } else {
        val southOfDitch = tile1.z < tile2.z

        if (southOfDitch) {
            directionAngle = Direction.NORTH.angle
        } else {
            directionAngle = Direction.SOUTH.angle
        }
    }

    return directionAngle
}
/*
suspend fun turn_to_corpse(it: QueueTask, n: Npc) {
    n.setTransmogId(if (n.id == 2788) Npcs.SHEEP_2691 else Npcs.SHEEP_2692)
    it.wait(100)
    n.setTransmogId(n.id) //npc dies + replaces original object in hunter
}*/