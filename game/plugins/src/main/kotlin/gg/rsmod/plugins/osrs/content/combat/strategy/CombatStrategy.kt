package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatStrategy {

    fun getAttackRange(weapon: Int): Int

    fun getHitDelay(start: Tile, target: Tile): Int
}