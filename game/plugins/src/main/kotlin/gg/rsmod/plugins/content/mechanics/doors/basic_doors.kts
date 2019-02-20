package gg.rsmod.plugins.content.mechanics.doors

import gg.rsmod.plugins.api.ext.*

val CLOSE_DOOR_SFX = 60
val OPEN_DOOR_SFX = 62

on_obj_option(obj = 1536, option = "close") {
    val player = it.player()
    val door = it.getInteractingGameObj()
    player.world.closeDoor(door)
    player.playSound(CLOSE_DOOR_SFX)
}

on_obj_option(obj = 1535, option = "open") {
    val player = it.player()
    val door = it.getInteractingGameObj()
    player.world.openDoor(door)
    player.playSound(OPEN_DOOR_SFX)
}