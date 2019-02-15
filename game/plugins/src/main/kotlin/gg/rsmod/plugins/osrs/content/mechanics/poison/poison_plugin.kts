package gg.rsmod.plugins.osrs.content.mechanics.poison

import gg.rsmod.game.model.POISON_TICKS_LEFT_ATTR
import gg.rsmod.game.model.POISON_TIMER
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.HitType
import gg.rsmod.plugins.osrs.api.ext.hit
import gg.rsmod.plugins.osrs.api.ext.pawn

val POISON_TICK_DELAY = 25

on_timer(POISON_TIMER) {
    val pawn = it.pawn()
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