package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.plugins.content.combat.isAttacking
import gg.rsmod.plugins.content.combat.isBeingAttacked
import gg.rsmod.plugins.content.magic.MagicSpells.on_magic_spell_button
import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport

val TERMINATE_HOME_TELEPORT_NEUTRAL: QueueTask.() -> Unit = {
    player.animate(-1)
    player.graphic(-1)
}

val TERMINATE_HOME_TELEPORT_SITTING: QueueTask.() -> Unit = {
    player.animate(4852)
    player.graphic(-1)
}

val HOME_TELEPORT_TIMER_ENABLED = true
val HOME_TELEPORT_TIMER_DELAY = 3000
val HOME_TELEPORT_TIMER = TimerKey(persistenceKey = "home_teleport_delay")

HomeTeleport.values.forEach { teleport ->

    on_magic_spell_button(teleport.spellName) {
        if (player.hasMoveDestination()) {
            player.message("You can't use that teleport at the moment.")
            return@on_magic_spell_button
        } else if (player.isAttacking()) {
            player.message("You can't use that teleport at the moment.")
            return@on_magic_spell_button
        }

        if (HOME_TELEPORT_TIMER_ENABLED && player.timers.has(HOME_TELEPORT_TIMER)) {
            val minutes = player.timers.getMinutesLeft(HOME_TELEPORT_TIMER)

            if (minutes != null) {
                player.message("You need to wait another ${minutes.appendToString("minute")} to cast this spell.")
            } else {
                player.message("You need to wait another couple of seconds to cast this spell.")
            }
            return@on_magic_spell_button
        }

        if (player.canTeleport(TeleportType.MODERN)) {
            player.queue(TaskPriority.STRONG) {
                teleport(teleport.endTile(world))
            }
        }
    }
}

suspend fun QueueTask.teleport(endTile: Tile) {
    wait(2)
    terminateAction = TERMINATE_HOME_TELEPORT_NEUTRAL
    player.animate(4847)
    player.graphic(800)
    player.playSound(193)
    if (!wait_and_check_combat(7)) {
        return
    }
    terminateAction = TERMINATE_HOME_TELEPORT_SITTING
    player.animate(4850)
    player.playSound(196)
    if (!wait_and_check_combat(6)) {
        return
    }
    player.animate(4853)
    player.graphic(802, 0)
    player.playSound(194)
    if (!wait_and_check_combat(4)) {
        return
    }
    player.animate(4855)
    player.graphic(803, 0)
    player.playSound(195)
    if (!wait_and_check_combat(3)) {
        return
    }
    player.graphic(804)
    if (!wait_and_check_combat(1)) {
        return
    }
    player.animate(4857)
    if (!wait_and_check_combat(2)) {
        return
    }
    player.animate(-1)
    player.moveTo(endTile)
    player.timers[HOME_TELEPORT_TIMER] = HOME_TELEPORT_TIMER_DELAY
}

suspend fun QueueTask.wait_and_check_combat(cycles: Int): Boolean {
    for (i in 0 until cycles) {
        wait(1)
        if (player.isBeingAttacked()) {
            terminate()
            return false
        }
    }
    return true
}

enum class HomeTeleport(val spellName: String, val endTile: World.() -> Tile) {
    LUMBRIDGE("Lumbridge Home Teleport", { gameContext.home }),
    EDGEVILLE("Edgeville Home Teleport", { gameContext.home }),
    LUNAR("Lunar Home Teleport", { gameContext.home }),
    ARCEUUS("Arceuus Home Teleport", { gameContext.home })
    ;

    companion object {
        val values = enumValues<HomeTeleport>()
    }
}