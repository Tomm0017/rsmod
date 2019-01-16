
import gg.rsmod.game.action.NpcPathAction
import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS
import gg.rsmod.game.model.FROZEN_TIMER
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.helper.pawn
import gg.rsmod.plugins.osrs.api.helper.setVarp
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs

r.bindCombat {
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
    val target = pawn.attr[COMBAT_TARGET_FOCUS] ?: return false

    pawn.facePawn(target)
    if (pawn is Player) {
        pawn.setVarp(Combat.PRIORITY_PID_VARP, target.index)
    }

    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)

    val pathFound = NpcPathAction.walkTo(it, pawn, target, attackRange)
    if (!pathFound) {
        pawn.movementQueue.clear()
        if (pawn is Player) {
            if (!pawn.timers.has(FROZEN_TIMER)) {
                pawn.message(Entity.YOU_CANT_REACH_THAT)
            }
            pawn.write(SetMinimapMarkerMessage(255, 255))
        }
        Combat.reset(pawn)
        return false
    }

    pawn.movementQueue.clear()

    if (pawn is Player) {
        // TODO: check if autocasting, if so wait 1 cycle before attacking
    }

    if (!pawn.timers.has(Combat.ATTACK_DELAY)) {
        strategy.attack(pawn, target)
        target.facePawn(pawn)
        pawn.timers[Combat.ATTACK_DELAY] = CombatConfigs.getAttackDelay(pawn)
    }
    return true
}