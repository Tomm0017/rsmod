package gg.rsmod.plugins.osrs.content.npcs.kbd

import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.cfg.Npcs
import gg.rsmod.plugins.osrs.api.ext.*
import gg.rsmod.plugins.osrs.content.combat.formula.DragonfireFormula
import gg.rsmod.plugins.osrs.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy

val MELEE_MAX_HIT = 26

spawnNpc(npc = Npcs.KING_BLACK_DRAGON, x = 2274, z = 4698, walkRadius = 5)

onNpcCombat(Npcs.KING_BLACK_DRAGON) {
    it.suspendable {
        combat(it)
    }
}

suspend fun combat(it: Plugin) {
    val npc = it.npc()
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            if (npc.world.chance(1, 4) && npc.canAttackMelee(it, target, moveIfNeeded = false)) {
                meleeAttack(npc, target)
            } else {
                when (npc.world.random(3)) {
                    0 -> fireAttack(npc, target)
                    1 -> poisonAttack(npc, target)
                    2 -> freezeAttack(npc, target)
                    3 -> shockAttack(npc, target)
                }
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.removeCombatTarget()
}

fun meleeAttack(npc: Npc, target: Pawn) {
    if (npc.world.chance(1, 2)) {
        // Headbutt attack
        npc.prepareAtttack(CombatClass.MELEE, CombatStyle.STAB, AttackStyle.ACCURATE)
        npc.animate(91)
    } else {
        // Claw attack
        npc.prepareAtttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.AGGRESSIVE)
        npc.animate(80)
    }
    val formula = MeleeCombatFormula
    if (formula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(npc.world.random(MELEE_MAX_HIT), delay = 1)
    } else {
        target.hit(damage = 0, delay = 1)
    }
}

fun fireAttack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 393, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(81)
    npc.world.spawn(projectile)
    target.hit(damage = npc.getRandomDamage(target, DragonfireFormula), delay = RangedCombatStrategy.getHitDelay(npc.getCentreTile(), target.getCentreTile()))
}

fun poisonAttack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 394, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(82)
    npc.world.spawn(projectile)
    target.hit(damage = npc.getRandomDamage(target, DragonfireFormula), delay = RangedCombatStrategy.getHitDelay(npc.getCentreTile(), target.getCentreTile())).addAction {
        // TODO: chance of poisoning for 8 dmg
    }
}

fun freezeAttack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 395, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(83)
    npc.world.spawn(projectile)
    target.hit(damage = npc.getRandomDamage(target, DragonfireFormula), delay = RangedCombatStrategy.getHitDelay(npc.getCentreTile(), target.getCentreTile())).addAction {
        if (target.world.chance(1, 6)) {
            target.freeze(cycles = 6) {
                if (target is Player) {
                    target.message("You have been frozen.")
                }
            }
        }
    }
}

fun shockAttack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 396, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
    npc.prepareAtttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(84)
    npc.world.spawn(projectile)
    target.hit(damage = npc.getRandomDamage(target, DragonfireFormula), delay = RangedCombatStrategy.getHitDelay(npc.getCentreTile(), target.getCentreTile())).addAction {
        if (target.world.chance(1, 6)) {
            if (target is Player) {
                arrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGED).forEach { skill ->
                    target.getSkills().alterCurrentLevel(skill, -2)
                }
                target.message("You're shocked and weakened!")
            }
        }
    }
}