package gg.rsmod.plugins.content.combat.formula

import gg.rsmod.game.model.attr.ANTIFIRE_POTION_CHARGES_ATTR
import gg.rsmod.game.model.attr.DRAGONFIRE_IMMUNITY_ATTR
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.NpcSpecies
import gg.rsmod.plugins.api.PrayerIcon
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.hasEquipped
import gg.rsmod.plugins.api.ext.hasPrayerIcon
import gg.rsmod.plugins.api.ext.isSpecies

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DragonfireFormula(val maxHit: Int, val minHit: Int = 0) : CombatFormula {

    override fun getAccuracy(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double): Double {
        return MagicCombatFormula.getAccuracy(pawn, target, specialAttackMultiplier)
    }

    override fun getMaxHit(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        var max = maxHit.toDouble()

        if (target is Player) {
            val magicProtection = target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MAGIC)
            val antiFirePotion = (target.attr[ANTIFIRE_POTION_CHARGES_ATTR] ?: 0) > 0
            val dragonFireImmunity = target.attr[DRAGONFIRE_IMMUNITY_ATTR] ?: false
            val antiFireShield = target.hasEquipped(EquipmentType.SHIELD, *ANTI_DRAGON_SHIELDS)
            val dragonfireShield = target.hasEquipped(EquipmentType.SHIELD, *DRAGONFIRE_SHIELDS)
            val wyvernShield = target.hasEquipped(EquipmentType.SHIELD, *WYVERN_SHIELDS)
            val dragonfireWard = target.hasEquipped(EquipmentType.SHIELD, *DRAGONFIRE_WARDS)

            if (dragonFireImmunity) {
                return minHit
            }

            if (pawn is Npc) {
                val basicDragon = pawn.isSpecies(NpcSpecies.BASIC_DRAGON)
                val brutalDragon = pawn.isSpecies(NpcSpecies.BRUTAL_DRAGON)

                if (magicProtection && basicDragon) {
                    max *= 0.35
                } else if (magicProtection && brutalDragon && antiFirePotion) {
                    return minHit
                }
            }

            if (antiFireShield || dragonfireShield || wyvernShield || dragonfireWard) {
                if (!antiFirePotion) {
                    max *= 0.25
                } else {
                    return minHit
                }
            }

            if (antiFirePotion) {
                max *= 0.20
            }
        }

        return Math.max(minHit, Math.floor(max).toInt())
    }

    companion object {
        private val ANTI_DRAGON_SHIELDS = intArrayOf(Items.ANTIDRAGON_SHIELD, Items.ANTIDRAGON_SHIELD_NZ)
        private val DRAGONFIRE_SHIELDS = intArrayOf(Items.DRAGONFIRE_SHIELD, Items.DRAGONFIRE_SHIELD_11284)
        private val WYVERN_SHIELDS = intArrayOf(Items.ANCIENT_WYVERN_SHIELD, Items.ANCIENT_WYVERN_SHIELD_21634)
        private val DRAGONFIRE_WARDS = intArrayOf(Items.DRAGONFIRE_WARD, Items.DRAGONFIRE_WARD_22003)
    }
}