package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun World.openDoor(obj: GameObject, opened: Int = obj.id + 1) {
    val oldRot = obj.rot
    val newRot = Math.abs((oldRot + 1) and 0x3)
    val newTile = when (oldRot) {
        0 -> obj.tile.transform(-1, 0)
        1 -> obj.tile.transform(0, 1)
        2 -> obj.tile.transform(1, 0)
        3 -> obj.tile.transform(0, -1)
        else -> throw IllegalStateException("Invalid door rotation: [currentRot=$oldRot, replaceRot=$newRot]")
    }

    val newDoor = DynamicObject(id = opened, type = obj.type, rot = newRot, tile = newTile)
    remove(obj)
    spawn(newDoor)
}

fun World.closeDoor(obj: GameObject, closed: Int = obj.id - 1) {
    val oldRot = obj.rot
    val newRot = Math.abs((oldRot - 1) and 0x3)
    val newTile = when (oldRot) {
        0 -> obj.tile.transform(0, 1)
        1 -> obj.tile.transform(1, 0)
        2 -> obj.tile.transform(0, -1)
        3 -> obj.tile.transform(-1, 0)
        else -> throw IllegalStateException("Invalid door rotation: [currentRot=$oldRot, replaceRot=$newRot]")
    }

    val newDoor = DynamicObject(id = closed, type = obj.type, rot = newRot, tile = newTile)
    remove(obj)
    spawn(newDoor)
}