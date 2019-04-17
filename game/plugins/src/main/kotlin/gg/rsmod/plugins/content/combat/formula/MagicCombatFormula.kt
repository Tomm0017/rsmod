package gg.rsmod.plugins.content.combat.formula

import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.*
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.content.mechanics.prayer.Prayer
import gg.rsmod.plugins.content.mechanics.prayer.Prayers

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatFormula : CombatFormula {

    private val BLACK_MASKS = intArrayOf(Items.BLACK_MASK,
            Items.BLACK_MASK_1, Items.BLACK_MASK_2, Items.BLACK_MASK_3, Items.BLACK_MASK_4,
            Items.BLACK_MASK_5, Items.BLACK_MASK_6, Items.BLACK_MASK_7, Items.BLACK_MASK_8,
            Items.BLACK_MASK_9, Items.BLACK_MASK_10)

    private val BLACK_MASKS_I = intArrayOf(Items.BLACK_MASK_I,
            Items.BLACK_MASK_1_I, Items.BLACK_MASK_2_I, Items.BLACK_MASK_3_I, Items.BLACK_MASK_4_I,
            Items.BLACK_MASK_5_I, Items.BLACK_MASK_6_I, Items.BLACK_MASK_7_I, Items.BLACK_MASK_8_I,
            Items.BLACK_MASK_9_I, Items.BLACK_MASK_10_I)

    private val SLAYER_HELM_I = intArrayOf(Items.SLAYER_HELMET_I, Items.BLACK_SLAYER_HELMET_I, Items.GREEN_SLAYER_HELMET_I,
            Items.PURPLE_SLAYER_HELMET_I, Items.RED_SLAYER_HELMET_I, Items.TURQUOISE_SLAYER_HELMET_I)

    private val MAGE_VOID = intArrayOf(Items.VOID_MAGE_HELM, Items.VOID_KNIGHT_TOP, Items.VOID_KNIGHT_ROBE, Items.VOID_KNIGHT_GLOVES)

    private val MAGE_ELITE_VOID = intArrayOf(Items.VOID_MAGE_HELM, Items.ELITE_VOID_TOP, Items.ELITE_VOID_ROBE, Items.VOID_KNIGHT_GLOVES)

    private val BOLT_SPELLS = enumSetOf(CombatSpell.WIND_BOLT, CombatSpell.WATER_BOLT, CombatSpell.EARTH_BOLT, CombatSpell.FIRE_BOLT)

    private val FIRE_SPELLS = enumSetOf(CombatSpell.FIRE_STRIKE, CombatSpell.FIRE_BOLT, CombatSpell.FIRE_BLAST, CombatSpell.FIRE_WAVE, CombatSpell.FIRE_SURGE)

    override fun getAccuracy(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double): Double {
        val attack = getAttackRoll(pawn)
        val defence = if (target is Player) getDefenceRoll(target) else if (target is Npc) getDefenceRoll(pawn, target) else throw IllegalArgumentException("Unhandled pawn.")

        val accuracy: Double
        if (attack > defence) {
            accuracy = 1.0 - (defence + 2.0) / (2.0 * (attack + 1.0))
        } else {
            accuracy = attack / (2.0 * (defence + 1))
        }
        return accuracy
    }

    override fun getMaxHit(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        val spell = pawn.attr[Combat.CASTING_SPELL]
        var hit = spell?.maxHit?.toDouble() ?: 1.0
        if (pawn is Player) {
            val magic = pawn.getSkills().getCurrentLevel(Skills.MAGIC)
            if (pawn.hasEquipped(EquipmentType.WEAPON, Items.TRIDENT_OF_THE_SEAS, Items.TRIDENT_OF_THE_SEAS_E, Items.TRIDENT_OF_THE_SEAS_FULL)) {
                hit = (Math.floor(magic / 3.0) - 5.0)
            } else if (pawn.hasEquipped(EquipmentType.WEAPON, Items.TRIDENT_OF_THE_SWAMP, Items.TRIDENT_OF_THE_SWAMP_E)) {
                hit = (Math.floor(magic / 3.0) - 2.0)
            }

            if (pawn.hasEquipped(EquipmentType.GLOVES, Items.CHAOS_GAUNTLETS) && spell != null && spell in BOLT_SPELLS) {
                hit += 3
            }

            var multiplier = 1.0 + (pawn.getMagicDamageBonus() / 100.0)

            if (pawn.hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_THE_DAMNED_FULL) && pawn.hasEquipped(EquipmentType.WEAPON, Items.AHRIMS_STAFF, Items.AHRIMS_STAFF_25, Items.AHRIMS_STAFF_50, Items.AHRIMS_STAFF_75, Items.AHRIMS_STAFF_100)
                    && pawn.world.chance(1, 4)) {
                multiplier += 0.3
            }

            if (pawn.hasEquipped(EquipmentType.WEAPON, Items.MYSTIC_SMOKE_STAFF) && pawn.hasSpellbook(Spellbook.STANDARD)) {
                multiplier += 0.1
            }

            if (pawn.hasEquipped(MAGE_ELITE_VOID)) {
                multiplier += 0.025
            }

            hit *= multiplier
            hit = Math.floor(hit)

            if (pawn.hasEquipped(EquipmentType.SHIELD, Items.TOME_OF_FIRE) && spell in FIRE_SPELLS) {
                // TODO: check tome of fire has charges
                hit *= 1.5
                hit = Math.floor(hit)
            }

            if (target is Npc) {
                if (pawn.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS_I) || pawn.hasEquipped(EquipmentType.HEAD, *SLAYER_HELM_I)) {
                    // TODO: check if on slayer task and target is slayer task
                    hit *= 1.15
                    hit = Math.floor(hit)
                } else if (pawn.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULETEI) && target.isSpecies(NpcSpecies.UNDEAD)) {
                    hit *= 1.20
                    hit = Math.floor(hit)
                }
            }
        } else if (pawn is Npc) {
            val multiplier = 1.0 + (pawn.getMagicDamageBonus() / 100.0)
            hit *= multiplier
            hit = Math.floor(hit)
        }

        hit *= getDamageDealMultiplier(pawn)
        hit = Math.floor(hit)

        return hit.toInt()
    }

    private fun getAttackRoll(pawn: Pawn): Int {
        val a = if (pawn is Player) getEffectiveAttackLevel(pawn) else if (pawn is Npc) getEffectiveAttackLevel(pawn) else 0.0
        val b = getEquipmentAttackBonus(pawn)

        var maxRoll = a * (b + 64.0)
        if (pawn is Player) {
            maxRoll = applyAttackSpecials(pawn, maxRoll)
        }
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(pawn: Pawn, target: Npc): Int {
        val a = if (pawn is Player) getEffectiveDefenceLevel(pawn) else if (pawn is Npc) getEffectiveDefenceLevel(pawn) else 0.0
        val b = getEquipmentDefenceBonus(target)

        val maxRoll = a * (b + 64.0)
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(target: Player): Int {
        var effectiveLvl = getEffectiveDefenceLevel(target)

        effectiveLvl *= 0.3
        effectiveLvl = Math.floor(effectiveLvl)

        var magicLvl = target.getSkills().getCurrentLevel(Skills.MAGIC).toDouble()
        magicLvl *= getPrayerAttackMultiplier(target)
        magicLvl = Math.floor(magicLvl)

        magicLvl *= 0.7
        magicLvl = Math.floor(magicLvl)

        val a = Math.floor(effectiveLvl + magicLvl).toInt()
        val b = getEquipmentDefenceBonus(target)

        val maxRoll = a * (b + 64.0)
        return maxRoll.toInt()
    }

    private fun applyAttackSpecials(player: Player, base: Double): Double {
        var hit = base

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        if (player.hasEquipped(EquipmentType.WEAPON, Items.MYSTIC_SMOKE_STAFF)) {
            hit *= 1.1
            hit = Math.floor(hit)
        }

        return hit
    }

    private fun getEffectiveAttackLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.MAGIC) * getPrayerAttackMultiplier(player))

        if (player.hasWeaponType(WeaponType.TRIDENT)) {
            effectiveLevel += when (CombatConfigs.getAttackStyle(player)) {
                AttackStyle.ACCURATE -> 3.0
                AttackStyle.CONTROLLED -> 1.0
                else -> 0.0
            }
        }

        effectiveLevel += 8.0

        if (player.hasEquipped(MAGE_VOID) || player.hasEquipped(MAGE_ELITE_VOID)) {
            effectiveLevel *= 1.45
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveDefenceLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.DEFENCE) * getPrayerDefenceMultiplier(player))

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.DEFENSIVE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            AttackStyle.LONG_RANGE -> 3.0
            else -> 0.0
        }

        effectiveLevel += 8.0

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveAttackLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.MAGIC).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveDefenceLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.DEFENCE).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEquipmentAttackBonus(pawn: Pawn): Double {
        return pawn.getBonus(BonusSlot.ATTACK_MAGIC).toDouble()
    }

    private fun getEquipmentDefenceBonus(target: Pawn): Double {
        return target.getBonus(BonusSlot.DEFENCE_MAGIC).toDouble()
    }

    private fun getEquipmentMultiplier(player: Player): Double = when {
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULET) -> 7.0 / 6.0
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULET_E) -> 1.2
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULETI) -> 1.15
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULETEI) -> 1.2
        // TODO: this should only apply when target is slayer task?
        player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS) -> 7.0 / 6.0
        player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS_I) -> 1.15
        else -> 1.0
    }

    private fun getPrayerAttackMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.MYSTIC_WILL) -> 1.05
        Prayers.isActive(player, Prayer.MYSTIC_LORE) -> 1.10
        Prayers.isActive(player, Prayer.MYSTIC_MIGHT) -> 1.15
        Prayers.isActive(player, Prayer.AUGURY) -> 1.25
        else -> 1.0
    }

    private fun getPrayerDefenceMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.THICK_SKIN) -> 1.05
        Prayers.isActive(player, Prayer.ROCK_SKIN) -> 1.10
        Prayers.isActive(player, Prayer.STEEL_SKIN) -> 1.15
        Prayers.isActive(player, Prayer.CHIVALRY) -> 1.20
        Prayers.isActive(player, Prayer.PIETY) -> 1.25
        Prayers.isActive(player, Prayer.RIGOUR) -> 1.25
        Prayers.isActive(player, Prayer.AUGURY) -> 1.25
        else -> 1.0
    }

    private fun getDamageDealMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_DEAL_MULTIPLIER] ?: 1.0
}