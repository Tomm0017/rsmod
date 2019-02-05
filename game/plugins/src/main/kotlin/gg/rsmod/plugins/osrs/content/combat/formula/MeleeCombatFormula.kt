package gg.rsmod.plugins.osrs.content.combat.formula

import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.BonusSlot
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.PrayerIcon
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.cfg.Items
import gg.rsmod.plugins.osrs.api.helper.getBonus
import gg.rsmod.plugins.osrs.api.helper.hasEquipped
import gg.rsmod.plugins.osrs.api.helper.hasPrayerIcon
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayer
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayers

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MeleeCombatFormula : CombatFormula {

    private val CHARGED_BLACK_MASKS = intArrayOf(
            Items.BLACK_MASK_1, Items.BLACK_MASK_2, Items.BLACK_MASK_3, Items.BLACK_MASK_4,
            Items.BLACK_MASK_5, Items.BLACK_MASK_6, Items.BLACK_MASK_7, Items.BLACK_MASK_8,
            Items.BLACK_MASK_9, Items.BLACK_MASK_10)

    private val MELEE_VOID = intArrayOf(Items.VOID_MELEE_HELM, Items.VOID_KNIGHT_TOP, Items.VOID_KNIGHT_ROBE, Items.VOID_KNIGHT_GLOVES)

    private val MELEE_ELITE_VOID = intArrayOf(Items.VOID_MELEE_HELM, Items.ELITE_VOID_TOP, Items.ELITE_VOID_ROBE, Items.VOID_KNIGHT_GLOVES)

    override fun landHit(pawn: Pawn, target: Pawn, multiplier: Double): Boolean {
        return true
    }

    override fun getMaxHit(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        val a = if (pawn is Player) getEffectiveLevel(pawn) else 0.0
        val b = if (pawn is Player) getEquipmentBonus(pawn) else 0.0

        var base = Math.floor(0.5 + a * (b + 64.0) / 640.0).toInt()
        if (pawn is Player) {
            base = applySpecials(pawn, target, base, specialAttackMultiplier, specialPassiveMultiplier)
        }
        return base
    }

    private fun applySpecials(pawn: Pawn, target: Pawn, base: Int, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        var hit = base.toDouble()

        if (pawn is Player) {
            hit *= getEquipmentMultiplier(pawn)
            hit = Math.floor(hit)
        }

        hit *= specialAttackMultiplier
        hit = Math.floor(hit)

        if (target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MELEE)) {
            hit *= 0.6
            hit = Math.floor(hit)
        }

        if (specialPassiveMultiplier == 1.0) {
            hit = applyPassiveMultiplier(pawn, target, hit)
            hit = Math.floor(hit)
        } else {
            hit *= specialPassiveMultiplier
            hit = Math.floor(hit)
        }

        hit *= getDamageDealMultiplier(pawn)
        hit = Math.floor(hit)

        hit *= getDamageTakeMultiplier(target)
        hit = Math.floor(hit)

        return hit.toInt()
    }

    private fun getEquipmentBonus(player: Player): Double = player.getBonus(BonusSlot.MELEE_STRENGTH).toDouble()

    private fun getEffectiveLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.STRENGTH) * getPrayerMultiplier(player))

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.AGGRESSIVE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            else -> 0.0
        }

        effectiveLevel += 8

        if (player.hasEquipped(MELEE_VOID) || player.hasEquipped(MELEE_ELITE_VOID)) {
            effectiveLevel *= 1.10
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return Math.floor(effectiveLevel)
    }

    private fun getPrayerMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.BURST_OF_STRENGTH) -> 1.05
        Prayers.isActive(player, Prayer.SUPERHUMAN_STRENGTH) -> 1.10
        Prayers.isActive(player, Prayer.ULTIMATE_STRENGTH) -> 1.15
        Prayers.isActive(player, Prayer.CHIVALRY) -> 1.18
        Prayers.isActive(player, Prayer.PIETY) -> 1.23
        else -> 1.0
    }

    private fun getEquipmentMultiplier(player: Player): Double = when {
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULET) -> 7.0 / 6.0
        player.hasEquipped(EquipmentType.AMULET, Items.SALVE_AMULET_E) -> 1.2
        player.hasEquipped(EquipmentType.HEAD, Items.BLACK_MASK, *CHARGED_BLACK_MASKS) -> 7.0 / 6.0
        else -> 1.0
    }

    private fun applyPassiveMultiplier(pawn: Pawn, target: Pawn, base: Double): Double {
        if (pawn is Player) {
            val world = pawn.world
            val multiplier = when {
                pawn.hasEquipped(EquipmentType.AMULET, Items.BERSERKER_NECKLACE) -> 1.2
                isWearingDharok(pawn) -> {
                    val lost = (pawn.getMaxHp() - pawn.getCurrentHp()) / 100.0
                    val max = pawn.getMaxHp() / 100.0
                    1.0 + (lost * max)
                }
                pawn.hasEquipped(EquipmentType.WEAPON, Items.GADDERHAMMER) && isShade(target) -> if (world.chance(1, 20)) 2.0 else 1.25
                pawn.hasEquipped(EquipmentType.WEAPON, Items.KERIS, Items.KERISP) && (isKalphite(target) || isScarab(target)) -> if (world.chance(1, 51)) 3.0 else (4.0 / 3.0)
                else -> 1.0
            }
            if (multiplier == 1.0 && isWearingVerac(pawn)) {
                return base + 1.0
            }
            return base * multiplier
        }
        return base
    }

    private fun getDamageDealMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_DEAL_MULTIPLIER] ?: 1.0

    private fun getDamageTakeMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_TAKE_MULTIPLIER] ?: 1.0

    private fun isDemon(pawn: Pawn): Boolean {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).combatDef.isDemon()
        }
        return false
    }

    private fun isShade(pawn: Pawn): Boolean {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).combatDef.isShade()
        }
        return false
    }

    private fun isKalphite(pawn: Pawn): Boolean {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).combatDef.isKalphite()
        }
        return false
    }

    private fun isScarab(pawn: Pawn): Boolean {
        if (pawn.getType().isNpc()) {
            return (pawn as Npc).combatDef.isScarab()
        }
        return false
    }

    private fun isWearingDharok(pawn: Pawn): Boolean {
        if (pawn.getType().isPlayer()) {
            val player = pawn as Player
            return player.hasEquipped(EquipmentType.HEAD, Items.DHAROKS_HELM, Items.DHAROKS_HELM_25, Items.DHAROKS_HELM_50, Items.DHAROKS_HELM_75, Items.DHAROKS_HELM_100)
                    && player.hasEquipped(EquipmentType.WEAPON, Items.DHAROKS_GREATAXE, Items.DHAROKS_GREATAXE_25, Items.DHAROKS_GREATAXE_50, Items.DHAROKS_GREATAXE_75, Items.DHAROKS_GREATAXE_100)
                    && player.hasEquipped(EquipmentType.CHEST, Items.DHAROKS_PLATEBODY, Items.DHAROKS_PLATEBODY_25, Items.DHAROKS_PLATEBODY_50, Items.DHAROKS_PLATEBODY_75, Items.DHAROKS_PLATEBODY_100)
                    && player.hasEquipped(EquipmentType.LEGS, Items.DHAROKS_PLATELEGS, Items.DHAROKS_PLATELEGS_25, Items.DHAROKS_PLATELEGS_50, Items.DHAROKS_PLATELEGS_75, Items.DHAROKS_PLATELEGS_100)
        }
        return false
    }

    private fun isWearingVerac(pawn: Pawn): Boolean {
        if (pawn.getType().isPlayer()) {
            val player = pawn as Player
            return player.hasEquipped(EquipmentType.HEAD, Items.VERACS_HELM, Items.VERACS_HELM_25, Items.VERACS_HELM_50, Items.VERACS_HELM_75, Items.VERACS_HELM_100)
                    && player.hasEquipped(EquipmentType.WEAPON, Items.VERACS_FLAIL, Items.VERACS_FLAIL_25, Items.VERACS_FLAIL_50, Items.VERACS_FLAIL_75, Items.VERACS_FLAIL_100)
                    && player.hasEquipped(EquipmentType.CHEST, Items.VERACS_BRASSARD, Items.VERACS_BRASSARD_25, Items.VERACS_BRASSARD_50, Items.VERACS_BRASSARD_75, Items.VERACS_BRASSARD_100)
                    && player.hasEquipped(EquipmentType.LEGS, Items.VERACS_PLATESKIRT, Items.VERACS_PLATESKIRT_25, Items.VERACS_PLATESKIRT_50, Items.VERACS_PLATESKIRT_75, Items.VERACS_PLATESKIRT_100)
        }
        return false
    }
}