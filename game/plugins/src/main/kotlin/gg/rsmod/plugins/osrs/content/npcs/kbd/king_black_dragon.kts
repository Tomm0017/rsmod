package gg.rsmod.plugins.osrs.content.npcs.kbd

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.cfg.Npcs
import gg.rsmod.plugins.osrs.api.ext.*
import gg.rsmod.plugins.osrs.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.osrs.content.combat.formula.MeleeCombatFormula

onNpcCombat(Npcs.KING_BLACK_DRAGON) {
    it.suspendable {
        combat(it)
    }
}

suspend fun combat(it: Plugin) {
    val npc = it.npc()
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target) && npc.moveToAttackRange(it, target, distance = 6, projectile = true)) {
        npc.facePawn(target)
        if (npc.isAttackDelayReady()) {
            if (npc.world.chance(1, 4) && npc.canAttackMelee(it, target, moveIfNeeded = false)) {
                meleeAttack(npc, target)
            } else {
                when (npc.world.random(3)) {
                    0 -> fireAttack(npc, target)
                    1 -> fireAttack(npc, target)
                    2 -> fireAttack(npc, target)
                    3 -> fireAttack(npc, target)
                }
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.facePawn(null)
    npc.removeCombatTarget()
}

fun meleeAttack(npc: Npc, target: Pawn) {
    npc.animate(80)
    target.hit(npc.getRandomDamage(target, MeleeCombatFormula))
}

fun fireAttack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(target, gfx = 393, startHeight = 3, endHeight = 3, delay = 50, angle = 15, lifespan = 100)
    npc.animate(81)
    npc.world.spawn(projectile)
    target.hit(damage = npc.getRandomDamage(target, MagicCombatFormula), delay = 2)
}