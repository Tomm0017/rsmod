package gg.rsmod.plugins.osrs.content.combat.formula

import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatFormula {

    fun landHit(pawn: Pawn, target: Pawn, multiplier: Double): Boolean

    fun getMaxHit(pawn: Pawn, target: Pawn, bonuses: FormulaBonuses): Int

    fun getDefaultBonuses(pawn: Pawn): FormulaBonuses
}