package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.WeaponType
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
        val world = pawn.world

        /**
         * A list of actions that will be executed upon this hit dealing damage
         * to the [target].
         */
        val hitActions = arrayListOf<Function0<Unit>>()
        hitActions.add { postDamage(pawn, target) }

        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)

        val accuracy = MeleeCombatFormula.getAccuracy(pawn, target)
        val maxHit = MeleeCombatFormula.getMaxHit(pawn, target)
        val landHit = accuracy >= world.randomDouble()

        val damage = if (landHit) world.random(maxHit) else 0

        target.hit(damage = damage, delay = 1).addActions(hitActions)
}
}