package gg.rsmod.plugins.content.mechanics.doors.basicdoor

import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*

val CLOSE_DOOR_SFX = 60
val OPEN_DOOR_SFX = 62

val DOORS = arrayOf(
        Door(closed = Objs.DOOR_1535, opened = Objs.DOOR_1536),
        Door(closed = Objs.DOOR_1540, opened = Objs.DOOR_1541),
        Door(closed = Objs.DOOR_24050, opened = Objs.DOOR_24051),
        Door(closed = Objs.DOOR_24054, opened = Objs.DOOR_24055),
        Door(closed = Objs.DOOR_24057, opened = Objs.DOOR_24058)
)

DOORS.forEach { door ->
    on_obj_option(obj = door.opened, option = "close") {
        val player = it.player()
        val obj = it.getInteractingGameObj()
        player.world.closeDoor(obj)
        player.playSound(CLOSE_DOOR_SFX)
    }

    on_obj_option(obj = door.closed, option = "open") {
        val player = it.player()
        val obj = it.getInteractingGameObj()
        player.world.openDoor(obj)
        player.playSound(OPEN_DOOR_SFX)
    }
}