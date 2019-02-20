package gg.rsmod.plugins.content.mechanics.doors.basicdoor

import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*

val CLOSE_DOOR_SFX = 60
val STUCK_DOOR_SFX = 61
val OPEN_DOOR_SFX = 62

val STICK_STATE = AttributeKey<StickState>()

/**
 * The amount of times a door can be opened or closed before it gets "stuck".
 */
val CHANGES_BEFORE_STICK = 6

/**
 * The amount of cycles that must go by before a door becomes "unstuck".
 */
val RESET_STICK_DELAY = 25

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
        if (!is_stuck(player.world, obj)) {
            val newDoor = player.world.closeDoor(obj)
            copy_stick_vars(obj, newDoor)
            add_stick_var(player.world, newDoor)
            player.playSound(CLOSE_DOOR_SFX)
        } else {
            player.message("The door seems to be stuck.")
            player.playSound(STUCK_DOOR_SFX)
        }
    }

    on_obj_option(obj = door.closed, option = "open") {
        val player = it.player()
        val obj = it.getInteractingGameObj()
        if (!is_stuck(player.world, obj)) {
            val newDoor = player.world.openDoor(obj)
            copy_stick_vars(obj, newDoor)
            add_stick_var(player.world, newDoor)
            player.playSound(OPEN_DOOR_SFX)
        } else {
            player.message("The door seems to be stuck.")
            player.playSound(STUCK_DOOR_SFX)
        }
    }
}

fun copy_stick_vars(from: GameObject, to: GameObject) {
    if (from.attr.has(STICK_STATE)) {
        to.attr[STICK_STATE] = from.attr[STICK_STATE]!!
    }
}

fun add_stick_var(world: World, obj: GameObject) {
    val currentChanges = get_stick_changes(obj)
    obj.attr[STICK_STATE] = StickState(currentChanges + 1, world.currentCycle)
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