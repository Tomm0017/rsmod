package gg.rsmod.plugins.osrs.content.combat.formula

/**
 * @param prayerMultiplier
 * Reserved for the prayer strength multiplier.
 *
 * @param equipmentMultiplier
 * Reserved for black mask and salve amulet multipliers.
 *
 * @param specialAttackMultiplier
 * Reserved for special attack multipliers.
 *
 * @param specialPassiveMultiplier
 * Reserved for special attack passive multipliers.
 *
 * @param castleWarsMultiplier
 * Reserved for castlewar brace multiplier.
 *
 * @param staffOfDeadMultiplier
 * Reserved for staff of the dead special attack effect multiplier.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class FormulaBonuses(val prayerMultiplier: Double, val equipmentMultiplier: Double,
                          val specialAttackMultiplier: Double, val specialPassiveMultiplier: Double,
                          val castleWarsMultiplier: Double, val staffOfDeadMultiplier: Double)