package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.hasWeaponType
import gg.rsmod.plugins.osrs.api.helper.hit
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
        hitActions.add { RangedCombatStrategy.postDamage(pawn, target) }

        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)

        val damage = if (landHit(pawn, target)) getMaxHit(pawn, target) else 0
        target.hit(damage = damage, delay = getHitDelay(pawn.calculateCentreTile(), target.calculateCentreTile()))
                .addActions(hitActions)
    }

    override fun getHitDelay(start: Tile, target: Tile): Int = 1

    private fun getMaxHit(pawn: Pawn, target: Pawn): Int {
        val bonuses = MeleeCombatFormula.getDefaultBonuses(pawn)
        return MeleeCombatFormula.getMaxHit(pawn, target, bonuses)
    }

    private fun landHit(pawn: Pawn, target: Pawn): Boolean = false
}