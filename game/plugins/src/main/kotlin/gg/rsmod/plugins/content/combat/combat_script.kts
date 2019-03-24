package gg.rsmod.plugins.content.combat

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.attr.FACING_PAWN_ATTR
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.STUN_TIMER
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell

set_combat_logic {
    pawn.queue {
        while (true) {
            if (!cycle(this)) {
                break
            }
            wait(1)
        }
    }
}

suspend fun cycle(it: QueueTask): Boolean {
    val pawn = it.pawn
    val target = pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()

    if (target == null) {
        pawn.resetFacePawn()
        return false
    }

    if (!pawn.lock.canAttack()) {
        return false
    }

    pawn.facePawn(target)

    if (!Combat.canEngage(pawn, target)) {
        return false
    }

    if (pawn is Player) {
        pawn.setVarp(Combat.PRIORITY_PID_VARP, target.index)

        if (!pawn.attr.has(Combat.CASTING_SPELL) && pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) != 0) {
            val spell = CombatSpell.values.firstOrNull { it.autoCastId == pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) }
            if (spell != null) {
                pawn.attr[Combat.CASTING_SPELL] = spell
            }
        }
    }

    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)

    val pathFound = PawnPathAction.walkTo(it, pawn, target, interactionRange = attackRange, lineOfSight = false)

    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
        return false
    }

    if (!pathFound) {
        pawn.stopMovement()
        if (pawn.getType().isNpc()) {
            /**
             * Npcs will keep trying to find a path to engage in combat.
             */
            return true
        }
        if (pawn is Player) {
            if (!pawn.timers.has(FROZEN_TIMER) || pawn.timers.has(STUN_TIMER)) {
                pawn.message(Entity.YOU_CANT_REACH_THAT)
            } else {
                pawn.message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
            }
            pawn.clearMapFlag()
        }
        pawn.resetFacePawn()
        Combat.reset(pawn)
        return false
    }

    pawn.stopMovement()

    if (Combat.isAttackDelayReady(pawn)) {
        if (Combat.canAttack(pawn, target, strategy)) {
            strategy.attack(pawn, target)
            Combat.postAttack(pawn, target)
        } else {
            Combat.reset(pawn)
            return false
        }
    }
    return true
}