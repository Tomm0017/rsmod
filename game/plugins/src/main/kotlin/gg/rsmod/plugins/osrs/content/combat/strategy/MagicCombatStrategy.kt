package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.api.ext.createProjectile
import gg.rsmod.plugins.osrs.api.ext.hit
import gg.rsmod.plugins.osrs.api.ext.playSound
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.osrs.content.mechanics.spells.SpellRequirements

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatStrategy : CombatStrategy {

    override fun getAttackRange(pawn: Pawn): Int = 10

    override fun canAttack(pawn: Pawn, target: Pawn): Boolean {
        if (pawn is Player) {
            val spell = pawn.attr[Combat.CASTING_SPELL]!!
            val requirements = SpellRequirements.getRequirements(spell.id)
            if (requirements != null && !SpellRequirements.canCast(pawn, requirements.lvl, requirements.items, requirements.spellbook)) {
                return false
            }
        }
        return true
    }

    override fun attack(pawn: Pawn, target: Pawn) {
        val world = pawn.world

        /**
         * A list of actions that will be executed upon this hit dealing damage
         * to the [target].
         */
        val hitActions = arrayListOf<Function0<Unit>>()
        hitActions.add { postDamage(pawn, target) }

        val spell = pawn.attr[Combat.CASTING_SPELL]!!
        val projectile = pawn.createProjectile(target, gfx = spell.projectile, type = ProjectileType.MAGIC, endHeight = spell.projectilEndHeight)

        pawn.animate(spell.castAnimation)
        spell.castGfx?.let { gfx -> pawn.graphic(gfx) }
        spell.impactGfx?.let { gfx -> target.graphic(Graphic(gfx.id, gfx.height, projectile.lifespan)) }
        if (spell.projectile > 0) {
            world.spawn(projectile)
        }

        if (pawn is Player) {
            if (spell.castSound != -1) {
                pawn.playSound(id = spell.castSound, volume = 1, delay = 0)
            }
            SpellRequirements.getRequirements(spell.id)?.let { requirement -> SpellRequirements.removeRunes(pawn, requirement.items) }
        }

        val accuracy = RangedCombatFormula.getAccuracy(pawn, target)
        val maxHit = RangedCombatFormula.getMaxHit(pawn, target)
        val landHit = accuracy >= world.randomDouble()

        val damage = if (landHit) world.random(maxHit) else 0

        target.hit(damage = damage, delay = getHitDelay(pawn.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize()  / 2)))
                .addActions(hitActions)
    }

    private fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + Math.floor((1.0 + distance) / 3.0).toInt()
    }
}