package gg.rsmod.plugins.content.mechanics.poison

import gg.rsmod.game.model.attr.POISON_TICKS_LEFT_ATTR
import gg.rsmod.game.model.timer.POISON_TIMER

val POISON_TICK_DELAY = 25

on_player_death {
    player.timers.remove(POISON_TIMER)
    Poison.setHpOrb(player, Poison.OrbState.NONE)
}

on_timer(POISON_TIMER) {
    val pawn = pawn
    val ticksLeft = pawn.attr[POISON_TICKS_LEFT_ATTR] ?: 0

    if (ticksLeft == 0) {
        if (pawn is Player) {
            Poison.setHpOrb(pawn, Poison.OrbState.NONE)
        }
        return@on_timer
    }

    if (ticksLeft > 0) {
        pawn.attr[POISON_TICKS_LEFT_ATTR] = ticksLeft - 1
        pawn.hit(damage = Poison.getDamageForTicks(ticksLeft), type = HitType.POISON)
    } else if (ticksLeft < 0) {
        pawn.attr[POISON_TICKS_LEFT_ATTR] = ticksLeft + 1
    }

    pawn.timers[POISON_TIMER] = POISON_TICK_DELAY
}