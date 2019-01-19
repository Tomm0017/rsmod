package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatStrategy : CombatStrategy {

    override fun getAttackRange(pawn: Pawn): Int = 10

    override fun canAttack(pawn: Pawn, target: Pawn): Boolean {
        return true
    }

    override fun attack(pawn: Pawn, target: Pawn) {
        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)
    }

    override fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + (Math.floor((1.0 + distance) / 3.0)).toInt()
    }

    override fun getMaxHit(pawn: Pawn): Int = pawn.world.random(10)

    override fun rollAccuracy(pawn: Pawn, target: Pawn): Boolean = pawn.world.chance(2, 1)
}