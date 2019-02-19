package gg.rsmod.plugins.content.combat.strategy

import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.combat.XpMode
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.HitType
import gg.rsmod.plugins.api.ProjectileType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.addXp
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.hit
import gg.rsmod.plugins.api.ext.playSound
import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.content.mechanics.spells.SpellRequirements

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

        val formula = MagicCombatFormula
        val accuracy = formula.getAccuracy(pawn, target)
        val maxHit = formula.getMaxHit(pawn, target)
        val landHit = accuracy >= world.randomDouble()
        val damage = if (landHit) world.random(maxHit) else 0

        if (damage > 0 && pawn.getType().isPlayer()) {
            addCombatXp(pawn as Player, target, damage)
        }

        target.hit(damage = damage, type = if (landHit) HitType.HIT else HitType.BLOCK, delay = getHitDelay(pawn.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2)))
                .addActions(hitActions).setCancelIf { pawn.isDead() }
    }

    fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + Math.floor((1.0 + distance) / 3.0).toInt()
    }

    private fun addCombatXp(player: Player, target: Pawn, damage: Int) {
        val mode = CombatConfigs.getXpMode(player)
        val multiplier = if (target is Npc) MeleeCombatStrategy.getNpcXpMultiplier(target) else 1.0

        if (mode == XpMode.MAGIC) {
            val defensive = player.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) != 0 && player.getVarbit(Combat.DEFENSIVE_MAGIC_CAST_VARBIT) != 0
            if (!defensive) {
                player.addXp(Skills.MAGIC, damage * 2.0 * multiplier)
                player.addXp(Skills.HITPOINTS, damage * 1.33 * multiplier)
            } else {
                player.addXp(Skills.MAGIC, damage * 1.33 * multiplier)
                player.addXp(Skills.DEFENCE, damage * multiplier)
                player.addXp(Skills.HITPOINTS, damage * 1.33 * multiplier)
            }
        } else if (mode == XpMode.SHARED) {
            player.addXp(Skills.MAGIC, damage * 1.33 * multiplier)
            player.addXp(Skills.DEFENCE, damage * multiplier)
            player.addXp(Skills.HITPOINTS, damage * 1.33 * multiplier)
        }
    }
}