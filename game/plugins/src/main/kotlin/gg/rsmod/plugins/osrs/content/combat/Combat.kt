package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.model.*
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.ext.getAttackStyle
import gg.rsmod.plugins.osrs.api.ext.getVarbit
import gg.rsmod.plugins.osrs.api.ext.hasWeaponType
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MagicCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Combat {

    val ATTACK_DELAY = TimerKey()

    val CASTING_SPELL = AttributeKey<CombatSpell>()
    val DAMAGE_DEAL_MULTIPLIER = AttributeKey<Double>()
    val DAMAGE_TAKE_MULTIPLIER = AttributeKey<Double>()
    val BOLT_ENCHANTMENT_EFFECT = AttributeKey<Boolean>()

    const val PRIORITY_PID_VARP = 1075

    const val SELECTED_AUTOCAST_VARBIT = 276
    const val DEFENSIVE_MAGIC_CAST_VARBIT = 2668

    fun reset(pawn: Pawn) {
        pawn.attr.remove(COMBAT_TARGET_FOCUS_ATTR)
    }

    fun canAttack(pawn: Pawn, target: Pawn, combatClass: CombatClass): Boolean = canEngage(pawn, target) && getStrategy(combatClass).canAttack(pawn, target)

    fun canAttack(pawn: Pawn, target: Pawn, strategy: CombatStrategy): Boolean = canEngage(pawn, target) && strategy.canAttack(pawn, target)

    fun isAttackDelayReady(pawn: Pawn): Boolean = !pawn.timers.has(Combat.ATTACK_DELAY)

    fun postAttack(pawn: Pawn, target: Pawn) {
        pawn.timers[ATTACK_DELAY] = CombatConfigs.getAttackDelay(pawn)
        target.timers[ACTIVE_COMBAT_TIMER] = 17 // 10,2 seconds
        pawn.attr[BOLT_ENCHANTMENT_EFFECT] = false

        pawn.attr[LAST_HIT_ATTR] = WeakReference(target)
        target.attr[LAST_HIT_BY_ATTR] = WeakReference(pawn)

        if (pawn.attr.has(CASTING_SPELL) && pawn is Player && pawn.getVarbit(SELECTED_AUTOCAST_VARBIT) == 0) {
            pawn.attr.remove(CASTING_SPELL)
            reset(pawn)
        }
    }

    fun raycast(pawn: Pawn, target: Pawn, distance: Int, projectile: Boolean): Boolean {
        val world = pawn.world
        val start = pawn.tile
        val end = target.tile

        return start.isWithinRadius(end, distance) && world.collision.raycast(start, end, projectile = projectile)
    }

    suspend fun moveToAttackRange(it: Plugin, pawn: Pawn, target: Pawn, distance: Int, projectile: Boolean): Boolean {
        val world = pawn.world
        val start = pawn.tile
        val end = target.tile

        val withinRange = start.isWithinRadius(end, distance) && world.collision.raycast(start, end, projectile = projectile)
        return withinRange || PawnPathAction.walkTo(it, pawn, target, interactionRange = distance)
    }

    fun getProjectileLifespan(source: Pawn, target: Tile, type: ProjectileType): Int = when (type) {
        ProjectileType.MAGIC -> {
            val fastPath = source.world.collision.raycastTiles(source.tile, target)
            5 + (fastPath * 10)
        }
        else -> {
            val distance = source.tile.getDistance(target)
            type.calculateLife(distance)
        }
    }

    fun canEngage(pawn: Pawn, target: Pawn): Boolean {
        if (pawn.isDead() || target.isDead()) {
            return false
        }

        // TODO: maxDistance should be 32 if in 'large' viewport mode
        val maxDistance = 16
        if (!pawn.tile.isWithinRadius(target.tile, maxDistance)) {
            return false
        }

        val pvp = pawn.getType().isPlayer() && target.getType().isPlayer()
        val pvm = pawn.getType().isPlayer() && target.getType().isNpc()

        if (pawn is Player) {
            if (!pawn.isOnline()) {
                return false
            }

            if (pawn.hasWeaponType(WeaponType.BULWARK) && pawn.getAttackStyle() == 3) {
                pawn.message("Your bulwark is in its defensive state and can't be used to attack.")
                return false
            }

            if (pawn.invisible && pvp) {
                pawn.message("You can't attack while invisible.")
                return false
            }
        } else if (pawn is Npc) {
            if (!pawn.isSpawned()) {
                return false
            }
        }

        if (target is Npc) {
            if (!target.isSpawned()) {
                return false
            }
            if (target.combatDef.hitpoints == -1) {
                (pawn as? Player)?.message("You can't attack this npc.")
                return false
            }
        } else if (target is Player) {
            if (!target.isOnline()) {
                return false
            }
        }

        if (pvp) {
            // TODO: must be within combat lvl range
            // TODO: make sure they're in wildy or in dangerous minigame
        }
        return true
    }

    private fun getStrategy(combatClass: CombatClass): CombatStrategy = when (combatClass) {
        CombatClass.MELEE -> MeleeCombatStrategy
        CombatClass.RANGED -> RangedCombatStrategy
        CombatClass.MAGIC -> MagicCombatStrategy
    }
}