package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatStrategy {

    fun getAttackRange(pawn: Pawn): Int

    fun canAttack(pawn: Pawn, target: Pawn): Boolean

    fun attack(pawn: Pawn, target: Pawn)

    fun getHitDelay(start: Tile, target: Tile): Int

    fun getMaxHit(pawn: Pawn): Int

    fun rollAccuracy(pawn: Pawn, target: Pawn): Boolean
}