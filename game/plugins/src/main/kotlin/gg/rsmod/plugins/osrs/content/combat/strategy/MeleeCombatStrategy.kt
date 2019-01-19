package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.hasWeaponType
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs

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
        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)
    }

    override fun getHitDelay(start: Tile, target: Tile): Int = 1

    override fun getMaxHit(pawn: Pawn): Int = pawn.world.random(10)

    override fun rollAccuracy(pawn: Pawn, target: Pawn): Boolean = pawn.world.chance(2, 1)
}