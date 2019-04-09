package gg.rsmod.plugins.content.npcs.kbd

import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.plugins.content.combat.*
import gg.rsmod.plugins.content.combat.formula.DragonfireFormula
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.mechanics.poison.poison

on_npc_combat(Npcs.KING_BLACK_DRAGON) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            if (world.chance(1, 4) && npc.canAttackMelee(it, target, moveIfNeeded = false)) {
                melee_attack(npc, target)
            } else {
                when (world.random(3)) {
                    0 -> fire_attack(npc, target)
                    1 -> poison_attack(npc, target)
                    2 -> freeze_attack(npc, target)
                    3 -> shock_attack(npc, target)
                }
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun melee_attack(npc: Npc, target: Pawn) {
    if (world.chance(1, 2)) {
        // Headbutt attack
        npc.prepareAtttack(CombatClass.MELEE, CombatStyle.STAB, AttackStyle.ACCURATE)
        npc.animate(91)
    } else {
        // Claw attack
        npc.prepareAtttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.AGGRESSIVE)
        npc.animate(80)
    }
    if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(world.random(26), type = HitType.HIT, delay = 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
    }
}

fun fire_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 393, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(81)
    world.spawn(projectile)
    npc.dealHit(target = target, formula = DragonfireFormula(maxHit = 65), delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1)
}

fun poison_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 394, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(82)
    world.spawn(projectile)
    val hit = npc.dealHit(target = target, formula = DragonfireFormula(maxHit = 65, minHit = 10), delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1) {
        if (it.landed() && world.chance(1, 6)) {
            target.poison(initialDamage = 8) {
                if (target is Player) {
                    target.message("You have been poisoned.")
                }
            }
        }
    }
    if (hit.blocked()) {
        target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
    }
}

fun freeze_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 395, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(83)
    world.spawn(projectile)
    val hit = npc.dealHit(target = target, formula = DragonfireFormula(maxHit = 65, minHit = 10), delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1) {
        if (it.landed() && world.chance(1, 6)) {
            target.freeze(cycles = 6) {
                if (target is Player) {
                    target.message("You have been frozen.")
                }
            }
        }
    }
    if (hit.blocked()) {
        target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
    }
}

fun shock_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 396, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(84)
    world.spawn(projectile)
    val hit = npc.dealHit(target = target, formula = DragonfireFormula(maxHit = 65, minHit = 12), delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1) {
        if (it.landed() && world.chance(1, 6)) {
            if (target is Player) {
                arrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGED).forEach { skill ->
                    target.getSkills().alterCurrentLevel(skill, -2)
                }
                target.message("You're shocked and weakened!")
            }
        }
    }
    if (hit.blocked()) {
        target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
    }
}