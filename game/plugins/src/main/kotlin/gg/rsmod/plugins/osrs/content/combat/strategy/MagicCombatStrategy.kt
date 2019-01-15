package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatStrategy : CombatStrategy {

    override fun getAttackRange(weapon: Int): Int {
        return 10
    }

    override fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + (Math.floor((1.0 + distance) / 3.0)).toInt()
    }
}