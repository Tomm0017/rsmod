package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MeleeCombatStrategy : CombatStrategy {

    override fun getAttackRange(pawn: Pawn): Int = 1

    override fun getAttackAnimation(pawn: Pawn): Int = 422

    override fun getHitDelay(start: Tile, target: Tile): Int = 1
}