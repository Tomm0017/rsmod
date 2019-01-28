package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.helper.getVarp
import gg.rsmod.plugins.osrs.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatStrategy {

    fun getAttackRange(pawn: Pawn): Int

    fun canAttack(pawn: Pawn, target: Pawn): Boolean

    fun attack(pawn: Pawn, target: Pawn)

    fun getHitDelay(start: Tile, target: Tile): Int

    fun postDamage(pawn: Pawn, target: Pawn) {
        if (target.isDead()) {
            return
        }

        if (target.getType().isNpc()) {
            if (target.attr[COMBAT_TARGET_FOCUS_ATTR] != pawn) {
                target.attack(pawn)
            }
        } else if (target is Player) {
            if (target.getVarp(AttackTab.AUTO_RETALIATE_VARP) != 0) {
                target.attack(pawn)
            }
        }
    }
}