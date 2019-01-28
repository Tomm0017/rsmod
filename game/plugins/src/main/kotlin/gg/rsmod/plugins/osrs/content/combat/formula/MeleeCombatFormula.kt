package gg.rsmod.plugins.osrs.content.combat.formula

import gg.rsmod.game.model.combat.AttackStyle
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

    override fun landHit(pawn: Pawn, target: Pawn, multiplier: Double): Boolean {
        return true
    }

    override fun getMaxHit(pawn: Pawn, target: Pawn, bonuses: FormulaBonuses): Int {
        val a = if (pawn is Player) getEffectiveLevel(pawn, bonuses) else 0.0
        val b = if (pawn is Player) getEquipmentBonus(pawn) else 0.0

        var base = Math.floor(0.5 + a * (b + 64.0) / 640.0).toInt()
        if (pawn is Player) {
            base = applySpecials(target, base, bonuses)
        }
        return base
    }

    override fun getDefaultBonuses(pawn: Pawn): FormulaBonuses {
        if (pawn is Player) {
            return FormulaBonuses(prayerMultiplier = getPrayerMultiplier(pawn), equipmentMultiplier = getEquipmentMultiplier(pawn),
                    specialAttackMultiplier = 1.0, specialPassiveMultiplier = 1.0, castleWarsMultiplier = 1.0, staffOfDeadMultiplier = 1.0)
        }
        return FormulaBonuses(prayerMultiplier = 1.0, equipmentMultiplier = 1.0, specialAttackMultiplier = 1.0,
                specialPassiveMultiplier = 1.0, castleWarsMultiplier = 1.0, staffOfDeadMultiplier = 1.0)
    }

    private fun applySpecials(target: Pawn, base: Int, bonuses: FormulaBonuses): Int {
        var hit = base.toDouble()

        hit *= bonuses.equipmentMultiplier
        hit = Math.floor(hit)

        hit *= bonuses.specialAttackMultiplier
        hit = Math.floor(hit)

        if (target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MELEE)) {
            hit *= 0.6
            hit = Math.floor(hit)
        }

        hit *= bonuses.specialPassiveMultiplier
        hit = Math.floor(hit)

        hit *= bonuses.castleWarsMultiplier
        hit = Math.floor(hit)

        hit *= bonuses.staffOfDeadMultiplier
        hit = Math.floor(hit)

        return hit.toInt()
    }

    private fun getEquipmentBonus(player: Player): Double = player.getBonus(BonusSlot.MELEE_STRENGTH).toDouble()

    private fun getEffectiveLevel(player: Player, bonuses: FormulaBonuses): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.STRENGTH) * bonuses.prayerMultiplier)

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.AGGRESSIVE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            else -> 0.0
        }

        effectiveLevel += 8

        // TODO: apply void bonus

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
}