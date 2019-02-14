package gg.rsmod.plugins.osrs.api.ext

import gg.rsmod.game.model.*
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.entity.Projectile
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.*
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.formula.CombatFormula
import gg.rsmod.plugins.osrs.content.mechanics.poison.Poison
import gg.rsmod.util.AabbUtil

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

fun Pawn.poison(initialDamage: Int, onPoison: () -> Unit) {
    if (!Poison.isImmune(this) && Poison.poison(this, initialDamage)) {
        if (this is Player) {
            Poison.setHpOrb(this, Poison.OrbState.POISON)
        }
        onPoison()
    }
}

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

fun Pawn.isAttacking(): Boolean = attr[COMBAT_TARGET_FOCUS_ATTR]?.get() != null

fun Pawn.isBeingAttacked(): Boolean = timers.has(ACTIVE_COMBAT_TIMER)

fun Pawn.getCombatTarget(): Pawn? = attr[COMBAT_TARGET_FOCUS_ATTR]?.get()

fun Pawn.removeCombatTarget() = attr.remove(COMBAT_TARGET_FOCUS_ATTR)

fun Pawn.canEngageCombat(target: Pawn): Boolean = Combat.canEngage(this, target)

fun Pawn.canAttack(target: Pawn, combatClass: CombatClass): Boolean = Combat.canAttack(this, target, combatClass)

fun Pawn.isAttackDelayReady(): Boolean = Combat.isAttackDelayReady(this)

fun Pawn.combatRaycast(target: Pawn, distance: Int, projectile: Boolean): Boolean = Combat.raycast(this, target, distance, projectile)

suspend fun Pawn.canAttackMelee(it: Plugin, target: Pawn, moveIfNeeded: Boolean): Boolean =
        AabbUtil.areBordering(tile.x, tile.z, getSize(), getSize(), target.tile.x, target.tile.z, target.getSize(), target.getSize())
                || moveIfNeeded && moveToAttackRange(it, target, distance = 0, projectile = false)

suspend fun Pawn.moveToAttackRange(it: Plugin, target: Pawn, distance: Int, projectile: Boolean): Boolean = Combat.moveToAttackRange(it, this, target, distance, projectile)

fun Pawn.postAttackLogic(target: Pawn) = Combat.postAttack(this, target)

fun Pawn.getRandomDamage(target: Pawn, formula: CombatFormula): Int {
    val accuracy = formula.getAccuracy(this, target)
    val maxHit = formula.getMaxHit(this, target)
    val landHit = accuracy >= world.randomDouble()

    return if (landHit) world.random(maxHit) else 0
}
