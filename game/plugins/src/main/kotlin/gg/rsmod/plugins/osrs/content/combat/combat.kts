
import gg.rsmod.game.action.NpcPathAction
import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.ACTIVE_COMBAT_TIMER
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.FROZEN_TIMER
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.getAttackStyle
import gg.rsmod.plugins.osrs.api.helper.hasWeaponType
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
    val target = pawn.attr[COMBAT_TARGET_FOCUS_ATTR] ?: return false

    if (!pawn.lock.canAttack()) {
        return false
    }

    pawn.facePawn(target)

    if (target.isDead()) {
        return false
    }

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
        if (canEngage(pawn, target) && strategy.canAttack(pawn, target)) {
            strategy.attack(pawn, target)
            pawn.timers[Combat.ATTACK_DELAY] = CombatConfigs.getAttackDelay(pawn)

            target.timers[ACTIVE_COMBAT_TIMER] = 17 // 10,2 seconds
        } else {
            Combat.reset(pawn)
        }
    }
    return true
}

fun canEngage(pawn: Pawn, target: Pawn): Boolean {
    if (pawn.isDead() || target.isDead()) {
        return false
    }

    if (!pawn.tile.isWithinRadius(target.tile, 32)) {
        return false
    }

    val pvp = pawn.getType().isPlayer() && target.getType().isPlayer()
    val pvm = pawn.getType().isPlayer() && target.getType().isNpc()

    if (pawn is Player) {
        if (!pawn.isOnline()) {
            return false
        }

        if (pawn.hasWeaponType(WeaponType.BULWARK) && pawn.getAttackStyle() == 3) {
            pawn.message("Your bulwark is in its defensive state and can't be used to attack.")
            return false
        }

        if (pawn.invisible && pvp) {
            pawn.message("You can't attack while invisible.")
            return false
        }
    } else if (pawn is Npc) {
        if (!pawn.isSpawned()) {
            return false
        }
    }

    if (target is Npc) {
        if (!target.isSpawned()) {
            return false
        }
        if (target.combatDef.hitpoints == -1) {
            (pawn as? Player)?.message("You can't attack this npc.")
            return false
        }
    } else if (target is Player) {
        if (!target.isOnline()) {
            return false
        }
    }

    if (pvp) {
        // TODO: must be within combat lvl range
        // TODO: make sure they're in wildy or in dangerous minigame
    }
    return true
}