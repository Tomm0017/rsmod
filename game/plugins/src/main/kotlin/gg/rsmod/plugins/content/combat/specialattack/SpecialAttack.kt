package gg.rsmod.plugins.content.combat.specialattack

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SpecialAttack(val energyRequired: Int, val attack: CombatContext.() -> Unit)