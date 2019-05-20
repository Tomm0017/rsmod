package gg.rsmod.plugins.content.magic

import gg.rsmod.game.fs.def.AnimDef
import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.plugins.api.ext.getWildernessLevel
import gg.rsmod.plugins.api.ext.message

fun Player.canTeleport(type: TeleportType): Boolean {
    val currWildLvl = tile.getWildernessLevel()
    val wildLvlRestriction = type.wildLvlRestriction

    if (!lock.canTeleport()) {
        return false
    }

    if (currWildLvl > wildLvlRestriction) {
        message("A mysterious force blocks your teleport spell!")
        message("You can't use this teleport after level $wildLvlRestriction wilderness.")
        return false
    }

    return true
}

fun Pawn.prepareForTeleport() {
    resetInteractions()
    clearHits()
}

fun Pawn.teleport(endTile: Tile, type: TeleportType) {
    lock = LockState.FULL_WITH_DAMAGE_IMMUNITY

    queue(TaskPriority.STRONG) {
        prepareForTeleport()

        animate(type.animation)
        type.graphic?.let {
            graphic(it)
        }

        wait(type.teleportDelay)

        moveTo(endTile)

        type.endAnimation?.let {
            animate(it)
        }

        type.endGraphic?.let {
            graphic(it)
        }

        type.endAnimation?.let {
            val def = world.definitions.get(AnimDef::class.java, it)
            wait(def.cycleLength)
        }

        animate(-1)
        unlock()
    }
}