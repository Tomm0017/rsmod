package gg.rsmod.plugins.content.combat.strategy

import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatStrategy {

    fun getAttackRange(pawn: Pawn): Int

    fun canAttack(pawn: Pawn, target: Pawn): Boolean

    fun attack(pawn: Pawn, target: Pawn)
}