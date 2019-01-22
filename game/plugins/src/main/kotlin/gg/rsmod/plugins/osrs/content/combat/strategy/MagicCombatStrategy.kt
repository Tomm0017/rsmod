package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.skills.magic.Magic

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatStrategy : CombatStrategy {

    override fun getAttackRange(pawn: Pawn): Int = 10

    override fun canAttack(pawn: Pawn, target: Pawn): Boolean {
        return true
    }

    override fun attack(pawn: Pawn, target: Pawn) {
        val spell = pawn.attr[Combat.CASTING_SPELL]!!
        val projectile = Combat.createProjectile(pawn, target, gfx = spell.projectile, type = ProjectileType.MAGIC)

        // TODO: GLOBAL SOUND
        pawn.animate(spell.castAnimation)
        pawn.graphic(spell.castGfx)
        target.graphic(Graphic(spell.impactGfx.id, spell.impactGfx.height, projectile.lifespan))
        pawn.world.spawn(projectile)

        if (pawn is Player) {
            val struct = Magic.combatStructs[spell]
            struct?.let {
                Magic.removeRunes(pawn, struct.items)
            }
        }
    }

    override fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + (Math.floor((1.0 + distance) / 3.0)).toInt()
    }

    override fun getMaxHit(pawn: Pawn): Int = pawn.world.random(10)

    override fun rollAccuracy(pawn: Pawn, target: Pawn): Boolean = pawn.world.chance(2, 1)
}