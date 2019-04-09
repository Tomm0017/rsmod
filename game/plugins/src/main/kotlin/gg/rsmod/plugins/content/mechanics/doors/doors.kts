package gg.rsmod.plugins.content.mechanics.doors

import gg.rsmod.game.model.collision.ObjectType
import gg.rsmod.plugins.service.doors.DoorService
import gg.rsmod.plugins.service.doors.DoorStickState
import gg.rsmod.plugins.service.doors.DoubleDoorSet

val CLOSE_DOOR_SFX = 60
val STUCK_DOOR_SFX = 61
val OPEN_DOOR_SFX = 62

val STICK_STATE = AttributeKey<DoorStickState>()

/**
 * The amount of times a door can be opened or closed before it gets "stuck".
 */
val CHANGES_BEFORE_STICK = 5

/**
 * The amount of cycles that must go by before a door becomes "unstuck".
 */
val RESET_STICK_DELAY = 25

on_world_init {
    world.getService(DoorService::class.java)?.let { service ->

        service.doors.forEach { door ->
            on_obj_option(obj = door.opened, option = "close") {
                val obj = player.getInteractingGameObj()
                if (!is_stuck(world, obj)) {
                    val newDoor = world.closeDoor(obj, closed = door.closed, invertTransform = obj.type == ObjectType.DIAGONAL_WALL.value)
                    copy_stick_vars(obj, newDoor)
                    add_stick_var(world, newDoor)
                    player.playSound(CLOSE_DOOR_SFX)
                } else {
                    player.message("The door seems to be stuck.")
                    player.playSound(STUCK_DOOR_SFX)
                }
            }

            on_obj_option(obj = door.closed, option = "open") {
                val obj = player.getInteractingGameObj()
                val newDoor = world.openDoor(obj, opened = door.opened, invertTransform = obj.type == ObjectType.DIAGONAL_WALL.value)
                copy_stick_vars(obj, newDoor)
                add_stick_var(world, newDoor)
                player.playSound(OPEN_DOOR_SFX)
            }
        }

        service.doubleDoors.forEach { doors ->
            on_obj_option(obj = doors.closed.left, option = "open") {
                handle_double_doors(player, player.getInteractingGameObj(), doors, open = true)
            }

            on_obj_option(obj = doors.closed.right, option = "open") {
                handle_double_doors(player, player.getInteractingGameObj(), doors, open = true)
            }

            on_obj_option(obj = doors.opened.left, option = "close") {
                handle_double_doors(player, player.getInteractingGameObj(), doors, open = false)
            }

            on_obj_option(obj = doors.opened.right, option = "close") {
                handle_double_doors(player, player.getInteractingGameObj(), doors, open = false)
            }
        }
    }
}

fun handle_double_doors(p: Player, obj: GameObject, doors: DoubleDoorSet, open: Boolean) {
    val left = obj.id == doors.opened.left || obj.id == doors.closed.left
    val right = obj.id == doors.opened.right || obj.id == doors.closed.right

    check(left || right)

    val otherDoorId = if (open) {
        if (left) doors.closed.right else doors.closed.left
    } else {
        if (left) doors.opened.right else doors.opened.left
    }
    val otherDoor = get_neighbour_door(world, obj, otherDoorId) ?: return

    if (!open && (is_stuck(world, obj) || is_stuck(world, otherDoor))) {
        p.message("The door seems to be stuck.")
        p.playSound(STUCK_DOOR_SFX)
        return
    }

    if (open) {
        val door1 = world.openDoor(obj, opened = if (left) doors.opened.left else doors.opened.right, invertRot = left)
        val door2 = world.openDoor(otherDoor, opened = if (left) doors.opened.right else doors.opened.left, invertRot = right)
        copy_stick_vars(obj, door1)
        add_stick_var(world, door1)
        copy_stick_vars(obj, door2)
        add_stick_var(world, door2)
        p.playSound(OPEN_DOOR_SFX)
    } else {
        val door1 = world.closeDoor(obj, closed = if (left) doors.closed.left else doors.closed.right, invertRot = left, invertTransform = left)
        val door2 = world.closeDoor(otherDoor, closed = if (left) doors.closed.right else doors.closed.left, invertRot = right, invertTransform = right)
        copy_stick_vars(obj, door1)
        add_stick_var(world, door1)
        copy_stick_vars(obj, door2)
        add_stick_var(world, door2)
        p.playSound(CLOSE_DOOR_SFX)
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

fun copy_stick_vars(from: GameObject, to: GameObject) {
    if (from.attr.has(STICK_STATE)) {
        to.attr[STICK_STATE] = from.attr[STICK_STATE]!!
    }
}

fun add_stick_var(world: World, obj: GameObject) {
    var currentChanges = get_stick_changes(obj)
    if (obj.attr.has(STICK_STATE) && Math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= RESET_STICK_DELAY) {
        currentChanges = 0
    }
    obj.attr[STICK_STATE] = DoorStickState(currentChanges + 1, world.currentCycle)
}

fun get_stick_changes(obj: GameObject): Int = obj.attr[STICK_STATE]?.changeCount ?: 0

fun is_stuck(world: World, obj: GameObject): Boolean {
    val stuck = get_stick_changes(obj) >= CHANGES_BEFORE_STICK
    if (stuck && Math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= RESET_STICK_DELAY) {
        obj.attr.remove(STICK_STATE)
        return false
    }
    return stuck
}