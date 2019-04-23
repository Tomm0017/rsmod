package gg.rsmod.plugins.content.combat

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.attr.LAST_HIT_ATTR
import gg.rsmod.game.model.attr.LAST_HIT_BY_ATTR
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.timer.ACTIVE_COMBAT_TIMER
import gg.rsmod.game.model.timer.ATTACK_DELAY
import gg.rsmod.plugins.api.BonusSlot
import gg.rsmod.plugins.api.NpcSkills
import gg.rsmod.plugins.api.ProjectileType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.WeaponType
import gg.rsmod.plugins.api.ext.closeInterface
import gg.rsmod.plugins.api.ext.getAttackBonus
import gg.rsmod.plugins.api.ext.getAttackStyle
import gg.rsmod.plugins.api.ext.getBonus
import gg.rsmod.plugins.api.ext.getStrengthBonus
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.getVarp
import gg.rsmod.plugins.api.ext.hasWeaponType
import gg.rsmod.plugins.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.content.combat.strategy.MagicCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.RangedCombatStrategy
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.content.inter.attack.AttackTab
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Combat {

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

    fun isAttackDelayReady(pawn: Pawn): Boolean = !pawn.timers.has(ATTACK_DELAY)

    fun postAttack(pawn: Pawn, target: Pawn) {
        pawn.timers[ATTACK_DELAY] = CombatConfigs.getAttackDelay(pawn)
        target.timers[ACTIVE_COMBAT_TIMER] = 17 // 10,2 seconds
        pawn.attr[BOLT_ENCHANTMENT_EFFECT] = false

        pawn.attr[LAST_HIT_ATTR] = WeakReference(target)
        target.attr[LAST_HIT_BY_ATTR] = WeakReference(pawn)

        if (pawn.attr.has(CASTING_SPELL) && pawn is Player && pawn.getVarbit(SELECTED_AUTOCAST_VARBIT) == 0) {
            reset(pawn)
            pawn.attr.remove(CASTING_SPELL)
        }

        if (target is Player && target.interfaces.getModal() != -1) {
            target.closeInterface(target.interfaces.getModal())
            target.interfaces.setModal(-1)
        }
    }

    fun postDamage(pawn: Pawn, target: Pawn) {
        if (target.isDead()) {
            return
        }

        val blockAnimation = CombatConfigs.getBlockAnimation(target)
        target.animate(blockAnimation)

        if (target.entityType.isNpc()) {
            if (!target.attr.has(COMBAT_TARGET_FOCUS_ATTR) || target.attr[COMBAT_TARGET_FOCUS_ATTR]!!.get() != pawn) {
                target.attack(pawn)
            }
        } else if (target is Player) {
            if (target.getVarp(AttackTab.DISABLE_AUTO_RETALIATE_VARP) == 0 && target.getCombatTarget() != pawn) {
                target.attack(pawn)
            }
        }
    }

    fun getNpcXpMultiplier(npc: Npc): Double {
        val attackLvl = npc.stats.getMaxLevel(NpcSkills.ATTACK)
        val strengthLvl = npc.stats.getMaxLevel(NpcSkills.STRENGTH)
        val defenceLvl = npc.stats.getMaxLevel(NpcSkills.DEFENCE)
        val hitpoints = npc.getMaxHp()

        val averageLvl = Math.floor((attackLvl + strengthLvl + defenceLvl + hitpoints) / 4.0)
        val averageDefBonus = Math.floor((npc.getBonus(BonusSlot.DEFENCE_STAB) + npc.getBonus(BonusSlot.DEFENCE_SLASH) + npc.getBonus(BonusSlot.DEFENCE_CRUSH)) / 3.0)
        return 1.0 + Math.floor(averageLvl * (averageDefBonus + npc.getStrengthBonus() + npc.getAttackBonus()) / 5120.0) / 40.0
    }

    fun raycast(pawn: Pawn, target: Pawn, distance: Int, projectile: Boolean): Boolean {
        val world = pawn.world
        val start = pawn.tile
        val end = target.tile

        return start.isWithinRadius(end, distance) && world.collision.raycast(start, end, projectile = projectile)
    }

    suspend fun moveToAttackRange(it: QueueTask, pawn: Pawn, target: Pawn, distance: Int, projectile: Boolean): Boolean {
        val world = pawn.world
        val start = pawn.tile
        val end = target.tile

        val srcSize = pawn.getSize()
        val dstSize = Math.max(distance, target.getSize())

        val touching = if (distance > 1) areOverlapping(start.x, start.z, srcSize, srcSize, end.x, end.z, dstSize, dstSize)
                        else areBordering(start.x, start.z, srcSize, srcSize, end.x, end.z, dstSize, dstSize)
        val withinRange = touching && world.collision.raycast(start, end, projectile = projectile)
        return withinRange || PawnPathAction.walkTo(it, pawn, target, interactionRange = distance, lineOfSight = false)
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

        val maxDistance = when {
            pawn is Player && pawn.hasLargeViewport() -> Player.LARGE_VIEW_DISTANCE
            else -> Player.NORMAL_VIEW_DISTANCE
        }
        if (!pawn.tile.isWithinRadius(target.tile, maxDistance)) {
            return false
        }

        val pvp = pawn.entityType.isPlayer() && target.entityType.isPlayer()
        val pvm = pawn.entityType.isPlayer() && target.entityType.isNpc()

        if (pawn is Player) {
            if (!pawn.isOnline) {
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
            if (!target.def.isAttackable() || target.combatDef.hitpoints == -1) {
                (pawn as? Player)?.message("You can't attack this npc.")
                return false
            }
            if (pawn is Player && target.combatDef.slayerReq > pawn.getSkills().getMaxLevel(Skills.SLAYER)) {
                pawn.message("You need a higher Slayer level to know how to wound this monster.")
                return false
            }
        } else if (target is Player) {
            if (!target.isOnline || target.invisible) {
                return false
            }

            if (!target.lock.canBeAttacked()) {
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

    private fun areOverlapping(x1: Int, z1: Int, width1: Int, length1: Int,
                       x2: Int, z2: Int, width2: Int, length2: Int): Boolean {
        val a = Box(x1, z1, width1 - 1, length1 - 1)
        val b = Box(x2, z2, width2 - 1, length2 - 1)

        if (a.x1 > b.x2 || b.x1 > a.x2) {
            return false
        }

        if (a.z1 > b.z2 || b.z1 > a.z2) {
            return false
        }

        return true
    }

    /**
     * Checks to see if two AABB are bordering, but not overlapping.
     */
    fun areBordering(x1: Int, z1: Int, width1: Int, length1: Int,
                     x2: Int, z2: Int, width2: Int, length2: Int): Boolean {
        val a = Box(x1, z1, width1 - 1, length1 - 1)
        val b = Box(x2, z2, width2 - 1, length2 - 1)

        if (b.x1 in a.x1 .. a.x2 && b.z1 in a.z1 .. a.z2 || b.x2 in a.x1 .. a.x2 && b.z2 in a.z1 .. a.z2) {
            return false
        }

        if (b.x1 > a.x2 + 1) {
            return false
        }

        if (b.x2 < a.x1 - 1) {
            return false
        }

        if (b.z1 > a.z2 + 1) {
            return false
        }

        if (b.z2 < a.z1 - 1) {
            return false
        }
        return true
    }

    data class Box(val x: Int, val z: Int, val width: Int, val length: Int) {

        val x1: Int get() = x

        val x2: Int get() = x + width

        val z1: Int get() = z

        val z2: Int get() = z + length
    }
}