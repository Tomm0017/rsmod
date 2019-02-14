package gg.rsmod.plugins.osrs.content.combat

import com.google.common.base.Stopwatch
import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.FROZEN_TIMER
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.ext.*
import gg.rsmod.plugins.osrs.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import java.util.concurrent.TimeUnit

val stopwatch = Stopwatch.createStarted()

on_client_cheat("max") {
    val player = it.player()
    player.attr[Combat.CASTING_SPELL] = CombatSpell.WIND_SURGE
    val accuracy = MagicCombatFormula.getAccuracy(player, player)
    val landHit = accuracy >= player.world.randomDouble()
    val max = MagicCombatFormula.getMaxHit(player, player)
    player.message("Max hit=$max - accuracy=$accuracy - land=$landHit")
}

on_combat {
    it.suspendable {
        while (true) {
            if (!cycle(it)) {
                break
            }
            it.wait(1)
        }
    }
}

suspend fun cycle(it: Plugin): Boolean {
    val pawn = it.pawn()
    val target = pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()

    if (target == null) {
        pawn.facePawn(null)
        return false
    }

    if (!pawn.lock.canAttack()) {
        return false
    }

    pawn.facePawn(target)

    if (target.isDead()) {
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

    val pathFound = PawnPathAction.walkTo(it, pawn, target, attackRange)
    if (!pathFound) {
        pawn.stopMovement()
        if (pawn.getType().isNpc()) {
            /**
             * Npcs will keep trying to find a path to engage in combat.
             */
            return true
        }
        if (pawn is Player) {
            if (!pawn.timers.has(FROZEN_TIMER)) {
                pawn.message(Entity.YOU_CANT_REACH_THAT)
            }
            pawn.clearMapFlag()
        }
        pawn.facePawn(null)
        Combat.reset(pawn)
        return false
    }

    pawn.stopMovement()

    if (Combat.isAttackDelayReady(pawn)) {
        if (Combat.canAttack(pawn, target, strategy)) {
            println("time since last att: ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} ms")
            stopwatch.reset().start()
            strategy.attack(pawn, target)
            Combat.postAttack(pawn, target)
        } else {
            Combat.reset(pawn)
        }
    }
    return true
}