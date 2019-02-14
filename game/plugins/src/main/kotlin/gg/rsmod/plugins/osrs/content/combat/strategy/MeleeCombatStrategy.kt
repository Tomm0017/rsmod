package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.combat.XpMode
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.ext.addXp
import gg.rsmod.plugins.osrs.api.ext.getRandomDamage
import gg.rsmod.plugins.osrs.api.ext.hasWeaponType
import gg.rsmod.plugins.osrs.api.ext.hit
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs
import gg.rsmod.plugins.osrs.content.combat.formula.MeleeCombatFormula

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MeleeCombatStrategy : CombatStrategy {

    override fun getAttackRange(pawn: Pawn): Int {
        if (pawn is Player) {
            val halberd = pawn.hasWeaponType(WeaponType.HALBERD)
            return if (halberd) 2 else 1
        }
        return 1
    }

    override fun canAttack(pawn: Pawn, target: Pawn): Boolean {
        return true
    }

    override fun attack(pawn: Pawn, target: Pawn) {
        /**
         * A list of actions that will be executed upon this hit dealing damage
         * to the [target].
         */
        val hitActions = arrayListOf<Function0<Unit>>()
        hitActions.add { postDamage(pawn, target) }

        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)

        val damage = pawn.getRandomDamage(target, MeleeCombatFormula)

        if (damage > 0 && pawn.getType().isPlayer()) {
            addCombatXp(pawn as Player, damage)
        }

        target.hit(damage = damage, delay = 1).addActions(hitActions).setCancelIf { target.isDead() }
    }

    private fun addCombatXp(player: Player, damage: Int) {
        val mode = CombatConfigs.getXpMode(player)

        when (mode) {
            XpMode.ATTACK -> {
                player.addXp(Skills.ATTACK, damage * 4.0)
                player.addXp(Skills.HITPOINTS, damage * 1.33)
            }
            XpMode.STRENGTH -> {
                player.addXp(Skills.STRENGTH, damage * 4.0)
                player.addXp(Skills.HITPOINTS, damage * 1.33)
            }
            XpMode.DEFENCE -> {
                player.addXp(Skills.DEFENCE, damage * 4.0)
                player.addXp(Skills.HITPOINTS, damage * 1.33)
            }
            XpMode.SHARED -> {
                player.addXp(Skills.ATTACK, damage * 1.33)
                player.addXp(Skills.STRENGTH, damage * 1.33)
                player.addXp(Skills.DEFENCE, damage * 1.33)
                player.addXp(Skills.HITPOINTS, damage * 1.33)
            }
        }
    }
}