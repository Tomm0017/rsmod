package gg.rsmod.plugins.content.mechanics.gates

import gg.rsmod.plugins.content.mechanics.doors.DoorStickState

val CLOSE_DOOR_SFX = 60
val STUCK_DOOR_SFX = 61
val OPEN_DOOR_SFX = 62

val STICK_STATE = AttributeKey<DoorStickState>()

val CHANGES_BEFORE_STICK_TAG = "opens_before_stick"
val RESET_STICK_DELAY_TAG = "reset_stuck_doors_delay"

/**
 * The amount of times a door can be opened or closed before it gets "stuck".
 */
val changesBeforeStick: Int
    get() = getProperty<Int>(CHANGES_BEFORE_STICK_TAG)!!

/**
 * The amount of cycles that must go by before a door becomes "unstuck".
 */
val resetStickDelay: Int
    get() = getProperty<Int>(RESET_STICK_DELAY_TAG)!!

load_metadata {
    propertyFileName = "gates"

    author = "Tomm"
    name = "General Gates"
    description = "Handle the opening and closing of general gates."

    properties(
            CHANGES_BEFORE_STICK_TAG to 5,
            RESET_STICK_DELAY_TAG to 25
    )
}

load_service(GateService())

on_world_init {
    world.getService(GateService::class.java)?.let { service ->
        service.gates.forEach { gate ->
            on_obj_option(obj = gate.closed.hinge, option = "open", lineOfSightDistance = 1) {
                open_gate(player, player.getInteractingGameObj(), gate)
            }

            on_obj_option(obj = gate.closed.extension, option = "open", lineOfSightDistance = 1) {
                open_gate(player, player.getInteractingGameObj(), gate)
            }

            on_obj_option(obj = gate.opened.hinge, option = "close", lineOfSightDistance = 1) {
                close_gate(player, player.getInteractingGameObj(), gate)
            }

            on_obj_option(obj = gate.opened.extension, option = "close", lineOfSightDistance = 1) {
                close_gate(player, player.getInteractingGameObj(), gate)
            }
        }
    }
}

fun open_gate(p: Player, obj: GameObject, gates: GateSet) {
    val oldRot = obj.rot

    val hinge = obj.id == gates.closed.hinge || obj.id == gates.opened.hinge
    val extension = obj.id == gates.closed.extension || obj.id == gates.opened.extension
    val otherGateId = if (hinge) gates.closed.extension else gates.closed.hinge

    val otherGate = get_neighbour_gate(world, obj, otherGateId) ?: return
    val hingeObj = if (hinge) obj else otherGate
    val extensionObj = if (extension) obj else otherGate

    val newHinge: DynamicObject
    val newExtension: DynamicObject
    when (oldRot) {
        0 -> {
            newHinge = DynamicObject(id = gates.opened.hinge, type = obj.type, rot = 3, tile = hingeObj.tile.transform(-1, 0))
            newExtension = DynamicObject(id = gates.opened.extension, type = obj.type, rot = 3, tile = hingeObj.tile.transform(-2, 0))
        }
        1 -> {
            newHinge = DynamicObject(id = gates.opened.hinge, type = obj.type, rot = 0, tile = hingeObj.tile.transform(0, 1))
            newExtension = DynamicObject(id = gates.opened.extension, type = obj.type, rot = 0, tile = hingeObj.tile.transform(0, 2))
        }
        2 -> {
            newHinge = DynamicObject(id = gates.opened.hinge, type = obj.type, rot = 1, tile = hingeObj.tile.transform(1, 0))
            newExtension = DynamicObject(id = gates.opened.extension, type = obj.type, rot = 1, tile = hingeObj.tile.transform(2, 0))
        }
        3 -> {
            newHinge = DynamicObject(id = gates.opened.hinge, type = obj.type, rot = 2, tile = hingeObj.tile.transform(0, -1))
            newExtension = DynamicObject(id = gates.opened.extension, type = obj.type, rot = 2, tile = hingeObj.tile.transform(0, -2))
        }
        else -> throw IllegalStateException("Invalid object rotation: $obj")
    }

    world.remove(obj)
    world.remove(otherGate)
    world.spawn(newHinge)
    world.spawn(newExtension)

    copy_stick_vars(hingeObj, newHinge)
    add_stick_var(world, newHinge)

    copy_stick_vars(extensionObj, newExtension)
    add_stick_var(world, newExtension)

    p.playSound(OPEN_DOOR_SFX)
}

fun close_gate(p: Player, obj: GameObject, gates: GateSet) {
    val oldRot = obj.rot

    val hinge = obj.id == gates.closed.hinge || obj.id == gates.opened.hinge
    val extension = obj.id == gates.closed.extension || obj.id == gates.opened.extension
    val otherGateId = if (hinge) gates.opened.extension else gates.opened.hinge

    val otherGate = get_neighbour_gate(world, obj, otherGateId) ?: return
    val hingeObj = if (hinge) obj else otherGate
    val extensionObj = if (extension) obj else otherGate

    if (is_stuck(world, obj) || is_stuck(world, otherGate)) {
        p.message("The door seems to be stuck.")
        p.playSound(STUCK_DOOR_SFX)
        return
    }

    val newHinge: DynamicObject
    val newExtension: DynamicObject
    when (oldRot) {
        0 -> {
            newHinge = DynamicObject(id = gates.closed.hinge, type = obj.type, rot = 1, tile = hingeObj.tile.transform(0, -1))
            newExtension = DynamicObject(id = gates.closed.extension, type = obj.type, rot = 1, tile = hingeObj.tile.transform(1, -1))
        }
        1 -> {
            newHinge = DynamicObject(id = gates.closed.hinge, type = obj.type, rot = 2, tile = hingeObj.tile.transform(-1, 0))
            newExtension = DynamicObject(id = gates.closed.extension, type = obj.type, rot = 2, tile = hingeObj.tile.transform(-1, -1))
        }
        2 -> {
            newHinge = DynamicObject(id = gates.closed.hinge, type = obj.type, rot = 3, tile = hingeObj.tile.transform(0, 1))
            newExtension = DynamicObject(id = gates.closed.extension, type = obj.type, rot = 3, tile = hingeObj.tile.transform(-1, 1))
        }
        3 -> {
            newHinge = DynamicObject(id = gates.closed.hinge, type = obj.type, rot = 0, tile = hingeObj.tile.transform(1, 0))
            newExtension = DynamicObject(id = gates.closed.extension, type = obj.type, rot = 0, tile = hingeObj.tile.transform(1, 1))
        }
        else -> throw IllegalStateException("Invalid object rotation: $obj")
    }

    world.remove(obj)
    world.remove(otherGate)
    world.spawn(newHinge)
    world.spawn(newExtension)

    copy_stick_vars(hingeObj, newHinge)
    add_stick_var(world, newHinge)

    copy_stick_vars(extensionObj, newExtension)
    add_stick_var(world, newExtension)

    p.playSound(CLOSE_DOOR_SFX)
}

fun get_neighbour_gate(world: World, obj: GameObject, otherGate: Int): GameObject? {
    val tile = obj.tile

    for (x in -1..1) {
        for (z in -1..1) {
            if (x == 0 && z == 0) {
                continue
            }
            val transform = tile.transform(x, z)
            val tileObj = world.getObject(transform, type = obj.type)
            if (tileObj?.id == otherGate) {
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
    if (obj.attr.has(STICK_STATE) && Math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= resetStickDelay) {
        currentChanges = 0
    }
    obj.attr[STICK_STATE] = DoorStickState(currentChanges + 1, world.currentCycle)
}

fun get_stick_changes(obj: GameObject): Int = obj.attr[STICK_STATE]?.changeCount ?: 0

fun is_stuck(world: World, obj: GameObject): Boolean {
    val stuck = get_stick_changes(obj) >= changesBeforeStick
    if (stuck && Math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= resetStickDelay) {
        obj.attr.remove(STICK_STATE)
        return false
    }
    return stuck
}