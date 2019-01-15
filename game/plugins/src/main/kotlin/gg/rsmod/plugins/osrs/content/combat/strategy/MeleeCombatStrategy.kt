package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MeleeCombatStrategy : CombatStrategy {

    override fun getAttackRange(weapon: Int): Int = 1

    override fun getHitDelay(start: Tile, target: Tile): Int = 1
}