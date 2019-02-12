package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.cfg.Items
import gg.rsmod.plugins.osrs.api.ext.getAttackStyle
import gg.rsmod.plugins.osrs.api.ext.getEquipment
import gg.rsmod.plugins.osrs.api.ext.hasEquipped
import gg.rsmod.plugins.osrs.api.ext.hasWeaponType
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MagicCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy

/**
 * @author Tom <rspsmods@gmail.com>
 */
object CombatConfigs {

    private var cachedItemStats: ItemStatsService? = null

    private const val PLAYER_DEFAULT_ATTACK_SPEED = 4

    private const val MIN_ATTACK_SPEED = 1

    fun getCombatStrategy(pawn: Pawn): CombatStrategy {
        if (pawn is Npc) {
            return when (pawn.combatClass) {
                CombatClass.MELEE -> MeleeCombatStrategy
                CombatClass.RANGED -> RangedCombatStrategy
                CombatClass.MAGIC -> MagicCombatStrategy
            }
        }

        if (pawn is Player) {
            return when {
                pawn.attr.has(Combat.CASTING_SPELL) -> MagicCombatStrategy
                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CHINCHOMPA, WeaponType.CROSSBOW, WeaponType.THROWN) -> RangedCombatStrategy
                else -> MeleeCombatStrategy
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getAttackDelay(pawn: Pawn): Int {
        if (pawn is Npc) {
            return pawn.combatDef.attackSpeed
        }

        if (pawn is Player) {
            val default = PLAYER_DEFAULT_ATTACK_SPEED
            val weapon = pawn.getEquipment(EquipmentType.WEAPON) ?: return default
            val stats = getItemStats(pawn.world) ?: return default
            val weaponStats = stats.get(weapon.id) ?: return default
            return Math.max(MIN_ATTACK_SPEED, weaponStats.attackSpeed)
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getAttackAnimation(pawn: Pawn): Int {
        if (pawn is Npc) {
            return when (pawn.combatClass) {
                CombatClass.MELEE -> pawn.combatDef.meleeAnimation
                CombatClass.RANGED -> pawn.combatDef.rangedAnimation
                CombatClass.MAGIC -> pawn.combatDef.magicAnimation
            }
        }

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
                pawn.hasWeaponType(WeaponType.CHINCHOMPA) -> 7618
                pawn.hasWeaponType(WeaponType.THROWN) -> if (pawn.hasEquipped(EquipmentType.WEAPON, Items.TOKTZXILUL)) 7558 else 929
                pawn.hasWeaponType(WeaponType.WHIP) -> 1658
                pawn.hasWeaponType(WeaponType.SPEAR) || pawn.hasWeaponType(WeaponType.HALBERD) -> if (style == 1) 440 else if (style == 2) 429 else 428
                pawn.hasWeaponType(WeaponType.CLAWS) -> 393
                else -> if (style == 1) 423 else 422
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getAttackStyle(pawn: Pawn): AttackStyle {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).attackStyle
        }

        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {

                pawn.hasWeaponType(WeaponType.NONE) -> when (style) {
                    0 -> AttackStyle.ACCURATE
                    1 -> AttackStyle.AGGRESSIVE
                    3 -> AttackStyle.DEFENSIVE
                    else -> AttackStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN, WeaponType.CHINCHOMPA) -> when (style) {
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

        throw IllegalArgumentException("Invalid pawn type.")
    }

    fun getCombatStyle(pawn: Pawn): CombatStyle {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).combatStyle
        }

        if (pawn is Player) {
            val style = pawn.getAttackStyle()

            return when {

                pawn.attr.has(Combat.CASTING_SPELL) -> CombatStyle.MAGIC

                pawn.hasWeaponType(WeaponType.NONE) -> when (style) {
                    0 -> CombatStyle.CRUSH
                    1 -> CombatStyle.CRUSH
                    3 -> CombatStyle.CRUSH
                    else -> CombatStyle.NONE
                }

                pawn.hasWeaponType(WeaponType.BOW, WeaponType.CROSSBOW, WeaponType.THROWN, WeaponType.CHINCHOMPA) -> CombatStyle.RANGED

                pawn.hasWeaponType(WeaponType.AXE) -> when (style) {
                    2 -> CombatStyle.CRUSH
                    else -> CombatStyle.SLASH
                }

                pawn.hasWeaponType(WeaponType.HAMMER) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.CLAWS) -> when (style) {
                    2 -> CombatStyle.STAB
                    else -> CombatStyle.SLASH
                }

                pawn.hasWeaponType(WeaponType.SALAMANDER) -> when (style) {
                    0 -> CombatStyle.SLASH
                    1 -> CombatStyle.RANGED
                    else -> CombatStyle.MAGIC
                }

                pawn.hasWeaponType(WeaponType.LONG_SWORD) -> when (style) {
                    2 -> CombatStyle.STAB
                    else -> CombatStyle.SLASH
                }

                pawn.hasWeaponType(WeaponType.TWO_HANDED) -> when (style) {
                    2 -> CombatStyle.CRUSH
                    else -> CombatStyle.SLASH
                }

                pawn.hasWeaponType(WeaponType.PICKAXE) -> when (style) {
                    2 -> CombatStyle.CRUSH
                    else -> CombatStyle.STAB
                }

                pawn.hasWeaponType(WeaponType.HALBERD) -> when (style) {
                    1 -> CombatStyle.SLASH
                    else -> CombatStyle.STAB
                }

                pawn.hasWeaponType(WeaponType.STAFF) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.SCYTHE) -> when (style) {
                    2 -> CombatStyle.CRUSH
                    else -> CombatStyle.SLASH
                }

                pawn.hasWeaponType(WeaponType.SPEAR) -> when (style) {
                    1 -> CombatStyle.SLASH
                    2 -> CombatStyle.CRUSH
                    else -> CombatStyle.STAB
                }

                pawn.hasWeaponType(WeaponType.MACE) -> when (style) {
                    2 -> CombatStyle.STAB
                    else -> CombatStyle.CRUSH
                }

                pawn.hasWeaponType(WeaponType.DAGGER) -> when (style) {
                    2 -> CombatStyle.SLASH
                    else -> CombatStyle.STAB
                }

                pawn.hasWeaponType(WeaponType.MAGIC_STAFF) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.WHIP) -> CombatStyle.SLASH

                pawn.hasWeaponType(WeaponType.STAFF_HALBERD) -> when (style) {
                    0 -> CombatStyle.STAB
                    1 -> CombatStyle.SLASH
                    else -> CombatStyle.CRUSH
                }

                pawn.hasWeaponType(WeaponType.TRIDENT) -> CombatStyle.MAGIC

                pawn.hasWeaponType(WeaponType.BLUDGEON) -> CombatStyle.CRUSH

                pawn.hasWeaponType(WeaponType.BULWARK) -> when (style) {
                    0 -> CombatStyle.CRUSH
                    else -> CombatStyle.NONE
                }

                else -> CombatStyle.NONE
            }
        }

        throw IllegalArgumentException("Invalid pawn type.")
    }

    private fun getItemStats(world: World): ItemStatsService? {
        if (cachedItemStats == null) {
            cachedItemStats = world.getService(ItemStatsService::class.java).orElse(null)
        }
        return cachedItemStats
    }
}