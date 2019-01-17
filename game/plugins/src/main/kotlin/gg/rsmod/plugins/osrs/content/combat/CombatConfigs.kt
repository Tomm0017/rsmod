package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.plugins.osrs.api.AttackStyle
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.getAttackStyle
import gg.rsmod.plugins.osrs.api.helper.getEquipment
import gg.rsmod.plugins.osrs.api.helper.hasWeaponType
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.RangedCombatStrategy

/**
 * @author Tom <rspsmods@gmail.com>
 */
object CombatConfigs {

    private var cachedItemStats: ItemStatsService? = null

    private const val PLAYER_DEFAULT_ATTACK_SPEED = 4

    private const val NPC_DEFAULT_ATTACK_SPEED = 4

    private const val MIN_ATTACK_SPEED = 1

    fun getCombatStrategy(pawn: Pawn): CombatStrategy {
        /**
         * TODO(Tom): add a way to specify what combat class npcs are currently using,
         * and also add a way to load in a custom combat script for npcs to use instead
         * of this script.
         */
        if (pawn !is Player) {
            return MeleeCombatStrategy
        }

        if (pawn.hasWeaponType(WeaponType.BOW, WeaponType.CHINCHOMPA, WeaponType.CROSSBOW, WeaponType.THROWN)) {
            return RangedCombatStrategy
        }

        return MeleeCombatStrategy
    }

    fun getAttackDelay(pawn: Pawn): Int {
        // TODO: get attack delay from npc combat defs
        if (pawn is Npc) {
            return NPC_DEFAULT_ATTACK_SPEED
        }
        if (pawn is Player) {
            val weapon = pawn.getEquipment(EquipmentType.WEAPON) ?: return PLAYER_DEFAULT_ATTACK_SPEED
            val stats = getItemStats(pawn.world) ?: return PLAYER_DEFAULT_ATTACK_SPEED
            val weaponStats = stats.get(weapon.id) ?: return PLAYER_DEFAULT_ATTACK_SPEED
            return Math.max(MIN_ATTACK_SPEED, weaponStats.attackSpeed)
        }
        return PLAYER_DEFAULT_ATTACK_SPEED
    }

    fun getAttackAnimation(pawn: Pawn): Int {
        // TODO: get attack animation for npcs
        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {
                pawn.hasWeaponType(WeaponType.AXE) -> if (style == 1) 401 else 395
                pawn.hasWeaponType(WeaponType.HAMMER) -> 401
                pawn.hasWeaponType(WeaponType.BULWARK) -> 7511
                pawn.hasWeaponType(WeaponType.SCYTHE) -> 8056
                pawn.hasWeaponType(WeaponType.BOW) -> 426
                pawn.hasWeaponType(WeaponType.CROSSBOW) -> 4230
                pawn.hasWeaponType(WeaponType.LONG_SWORD) -> if (style == 2) 386 else 390
                pawn.hasWeaponType(WeaponType.TWO_HANDED) -> if (style == 2) 406 else 407
                pawn.hasWeaponType(WeaponType.PICKAXE) -> if (style == 2) 400 else 401
                pawn.hasWeaponType(WeaponType.DAGGER) -> if (style == 2) 390 else 386
                pawn.hasWeaponType(WeaponType.MAGIC_STAFF) || pawn.hasWeaponType(WeaponType.STAFF) -> 419
                pawn.hasWeaponType(WeaponType.MACE) -> if (style == 2) 400 else 401
                pawn.hasWeaponType(WeaponType.THROWN) -> 929
                pawn.hasWeaponType(WeaponType.WHIP) -> 1658
                pawn.hasWeaponType(WeaponType.SPEAR) || pawn.hasWeaponType(WeaponType.HALBERD) -> if (style == 1) 440 else if (style == 2) 429 else 428
                pawn.hasWeaponType(WeaponType.CLAWS) -> 493
                else -> if (style == 1) 423 else 422
            }
        }
        return -1
    }

    fun getAttackStyle(pawn: Pawn): AttackStyle {
        // TODO: get attack style for npcs
        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {

                pawn.hasWeaponType(WeaponType.NONE) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.AGGRESSIVE
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.RAPID
                    3 -> AttackStyle.LONG_RANGE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.TRIDENT) -> when (style) {
                    0, 1 -> AttackStyle.ACCURATE
                    3 -> AttackStyle.LONG_RANGE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.AXE, WeaponType.HAMMER, WeaponType.TWO_HANDED, WeaponType.PICKAXE,
                        WeaponType.DAGGER, WeaponType.MAGIC_STAFF, WeaponType.LONG_SWORD, WeaponType.MAGIC_STAFF,
                        WeaponType.CLAWS) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.AGGRESSIVE
                    2 -> AttackStyle.CONTROLLED
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.SPEAR) -> when (style) {
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.CONTROLLED
                }

                pawn.hasWeaponType(WeaponType.HALBERD) -> when (style) {
                    0 -> AttackStyle.CONTROLLED
                    1 -> AttackStyle.AGGRESSIVE
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.SCYTHE) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.AGGRESSIVE
                    2 -> AttackStyle.AGGRESSIVE
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.WHIP) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.CONTROLLED
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.BLUDGEON) -> when (style) {
                    0, 1, 3 -> AttackStyle.AGGRESSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.BULWARK) -> AttackStyle.ACCURATE

                else -> AttackStyle.NONE
            }
        }
        return AttackStyle.NONE
    }

    private fun getItemStats(world: World): ItemStatsService? {
        if (cachedItemStats == null) {
            cachedItemStats = world.getService(ItemStatsService::class.java, searchSubclasses = false).orElse(null)
        }
        return cachedItemStats
    }
}