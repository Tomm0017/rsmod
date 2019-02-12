package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.model.ACTIVE_COMBAT_TIMER
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.Hit
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Projectile
import gg.rsmod.plugins.osrs.api.*
import gg.rsmod.plugins.osrs.content.combat.Combat

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Pawn.createProjectile(target: Tile, gfx: Int, type: ProjectileType, endHeight: Int = -1): Projectile {
    val builder = Projectile.Builder()
            .setTiles(start = tile, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = type.startHeight, endHeight = if (endHeight != -1) endHeight else type.endHeight)
            .setSlope(angle = type.angle, steepness = type.steepness)
            .setTimes(delay = type.delay, lifespan = type.delay + Combat.getProjectileLifespan(this, target, type))

    return builder.build()
}

fun Pawn.createProjectile(target: Pawn, gfx: Int, type: ProjectileType, endHeight: Int = -1): Projectile {
    val builder = Projectile.Builder()
            .setTiles(start = tile, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = type.startHeight, endHeight = if (endHeight != -1) endHeight else type.endHeight)
            .setSlope(angle = type.angle, steepness = type.steepness)
            .setTimes(delay = type.delay, lifespan = type.delay + Combat.getProjectileLifespan(this, target.tile, type))

    return builder.build()
}

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

fun Pawn.isAttacking(): Boolean = attr.has(COMBAT_TARGET_FOCUS_ATTR)

fun Pawn.isBeingAttacked(): Boolean = timers.has(ACTIVE_COMBAT_TIMER)