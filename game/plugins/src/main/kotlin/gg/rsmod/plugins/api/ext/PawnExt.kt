package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.Hit
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.plugins.api.BonusSlot
import gg.rsmod.plugins.api.HitType
import gg.rsmod.plugins.api.HitbarType
import gg.rsmod.plugins.api.PrayerIcon

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Pawn.hasPrayerIcon(icon: PrayerIcon): Boolean = prayerIcon == icon.id

fun Pawn.getBonus(slot: BonusSlot): Int = equipmentBonuses[slot.id]

fun Pawn.hit(damage: Int, type: HitType = if (damage == 0) HitType.BLOCK else HitType.HIT, delay: Int = 0): Hit {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage, type = type.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.pixelsWide)
            .build()

    pendingHits.add(hit)
    return hit
}

fun Pawn.doubleHit(damage1: Int, damage2: Int, delay: Int = 0, type1: HitType = if (damage1 == 0) HitType.BLOCK else HitType.HIT,
                   type2: HitType = if (damage2 == 0) HitType.BLOCK else HitType.HIT): Hit {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage1, type = type1.id)
            .addHit(damage = damage2, type = type2.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.pixelsWide)
            .build()

    pendingHits.add(hit)
    return hit
}

fun Pawn.showHitbar(percentage: Int, type: HitbarType) {
    pendingHits.add(Hit.Builder().onlyShowHitbar().setHitbarType(type.id).setHitbarPercentage(percentage).setHitbarMaxPercentage(type.pixelsWide).build())
}

fun Pawn.freeze(cycles: Int, onFreeze: () -> Unit) {
    if (timers.has(FROZEN_TIMER)) {
        return
    }
    stopMovement()
    timers[FROZEN_TIMER] = cycles
    onFreeze()
}