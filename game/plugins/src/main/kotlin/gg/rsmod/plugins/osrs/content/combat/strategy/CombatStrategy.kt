package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.BonusSlot
import gg.rsmod.plugins.osrs.api.ext.getAttackBonus
import gg.rsmod.plugins.osrs.api.ext.getBonus
import gg.rsmod.plugins.osrs.api.ext.getStrengthBonus
import gg.rsmod.plugins.osrs.api.ext.getVarp
import gg.rsmod.plugins.osrs.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatStrategy {

    fun getAttackRange(pawn: Pawn): Int

    fun canAttack(pawn: Pawn, target: Pawn): Boolean

    fun attack(pawn: Pawn, target: Pawn)

    fun postDamage(pawn: Pawn, target: Pawn) {
        if (target.isDead()) {
            return
        }

        if (target.getType().isNpc()) {
            if (!target.attr.has(COMBAT_TARGET_FOCUS_ATTR) || target.attr[COMBAT_TARGET_FOCUS_ATTR]!!.get() != pawn) {
                target.attack(pawn)
            }
        } else if (target is Player) {
            if (target.getVarp(AttackTab.AUTO_RETALIATE_VARP) != 0) {
                target.attack(pawn)
            }
        }
    }

    fun getNpcXpMultiplier(npc: Npc): Double {
        val def = npc.combatDef
        val averageLvl = Math.floor((def.attackLvl + def.strengthLvl + def.defenceLvl + def.hitpoints) / 4.0)
        val averageDefBonus = Math.floor((npc.getBonus(BonusSlot.DEFENCE_STAB) + npc.getBonus(BonusSlot.DEFENCE_SLASH) + npc.getBonus(BonusSlot.DEFENCE_CRUSH)) / 3.0)
        return 1.0 + Math.floor(averageLvl * (averageDefBonus + npc.getStrengthBonus() + npc.getAttackBonus()) / 5120.0) / 40.0
    }
}