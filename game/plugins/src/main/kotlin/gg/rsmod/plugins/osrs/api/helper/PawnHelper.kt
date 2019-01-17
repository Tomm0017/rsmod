package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.model.Hit
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.plugins.osrs.api.HitType
import gg.rsmod.plugins.osrs.api.HitbarType

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Pawn.hit(damage: Int, type: HitType = if (damage == 0) HitType.BLOCK else HitType.HIT, delay: Int = 0) {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage, type = type.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.percentage)
            .build()

    pendingHits.add(hit)
}

fun Pawn.doubleHit(damage1: Int, damage2: Int, delay: Int = 0,
                   type1: HitType = if (damage1 == 0) HitType.BLOCK else HitType.HIT,
                   type2: HitType = if (damage2 == 0) HitType.BLOCK else HitType.HIT) {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage1, type = type1.id)
            .addHit(damage = damage2, type = type2.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.percentage)
            .build()

    pendingHits.add(hit)
}

fun Pawn.showHitbar(percentage: Int, type: HitbarType) {
    pendingHits.add(Hit.Builder().onlyShowHitbar().setHitbarType(type.id).setHitbarPercentage(percentage).setHitbarMaxPercentage(type.percentage).build())
}