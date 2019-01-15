
import gg.rsmod.game.action.CombatPathAction
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.getVarp
import gg.rsmod.plugins.osrs.api.helper.pawn
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy

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

    val strategy = getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)
    val pathFound = CombatPathAction.walkTo(it, pawn, target, attackRange)

    if (!pathFound) {
        pawn.movementQueue.clear()
        if (pawn is Player) {
            pawn.message(Entity.YOU_CANT_REACH_THAT)
        }
        return false
    }

    pawn.movementQueue.clear()

    attack(pawn, target, strategy)
    pawn.timers[Combat.ATTACK_DELAY] = 3

    return true
}

fun attack(pawn: Pawn, target: Pawn, strategy: CombatStrategy) {
    val animation = strategy.getAttackAnimation(pawn)

    pawn.animate(animation)
    target.facePawn(pawn)
}

fun getCombatStrategy(pawn: Pawn): CombatStrategy {
    /**
     * TODO(Tom): add a way to specify what combat class npcs are currently using,
     * and also add a way to load in a custom combat script for npcs to use instead
     * of this script.
     */
    if (pawn !is Player) {
        return MeleeCombatStrategy
    }

    val weaponType = pawn.getVarp(843)
    when (weaponType) {
        WeaponType.BOW.id, WeaponType.CHINCHOMPA.id, WeaponType.CROSSBOW.id,
        WeaponType.THROWN.id -> return RangedCombatStrategy
    }
    return MeleeCombatStrategy
}