package gg.rsmod.plugins.content.mechanics.doors.doubledoor

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.ext.*

val CLOSE_DOOR_SFX = 60
val OPEN_DOOR_SFX = 62

val DOORS = arrayOf(
        Door(closed = DoubleDoor(left = 1521, right = 1524), opened = DoubleDoor(left = 1522, right = 1525)),
        Door(closed = DoubleDoor(left = 2546, right = 2548), opened = DoubleDoor(left = 2547, right = 2549)),
        Door(closed = DoubleDoor(left = 25814, right = 25813), opened = DoubleDoor(left = 25816, right = 25815)),
        Door(closed = DoubleDoor(left = 24059, right = 24064), opened = DoubleDoor(left = 24065, right = 24066))
)

DOORS.forEach { doors ->
    on_obj_option(obj = doors.closed.left, option = "open") {
        handle_doors(it.player(), it.getInteractingGameObj(), doors, open = true)
    }

    on_obj_option(obj = doors.closed.right, option = "open") {
        handle_doors(it.player(), it.getInteractingGameObj(), doors, open = true)
    }

    on_obj_option(obj = doors.opened.left, option = "close") {
        handle_doors(it.player(), it.getInteractingGameObj(), doors, open = false)
    }

    on_obj_option(obj = doors.opened.right, option = "close") {
        handle_doors(it.player(), it.getInteractingGameObj(), doors, open = false)
    }
}

fun handle_doors(p: Player, obj: GameObject, doors: Door, open: Boolean) {
    val left = obj.id == doors.opened.left || obj.id == doors.closed.left
    val right = obj.id == doors.opened.right || obj.id == doors.closed.right

    check(left || right)

    val otherDoorId = if (open) {
        if (left) doors.closed.right else doors.closed.left
    } else {
        if (left) doors.opened.right else doors.opened.left
    }
    val otherDoor = get_neighbour_door(p.world, obj, otherDoorId)

    if (otherDoor != null) {
        if (open) {
            p.world.openDoor(obj, opened = if (left) doors.opened.left else doors.opened.right, invertRot = left)
            p.world.openDoor(otherDoor, opened = if (left) doors.opened.right else doors.opened.left, invertRot = right)
            p.playSound(OPEN_DOOR_SFX)
        } else {
            p.world.closeDoor(obj, closed = if (left) doors.closed.left else doors.closed.right, invertRot = left, invertTransform = left)
            p.world.closeDoor(otherDoor, closed = if (left) doors.closed.right else doors.closed.left, invertRot = right, invertTransform = right)
            p.playSound(CLOSE_DOOR_SFX)
        }
    }
}

fun get_neighbour_door(world: World, obj: GameObject, otherDoor: Int): GameObject? {
    val tile = obj.tile

    for (x in -1..1) {
        for (z in -1..1) {
            if (x == 0 && z == 0) {
                continue
            }
            val transform = tile.transform(x, z)
            val tileObj = world.getObject(transform, type = obj.type)
            if (tileObj?.id == otherDoor) {
                return tileObj
            }
        }
    }
    return null
}