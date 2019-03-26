package gg.rsmod.game.model.combat

/**
 * Represents a valid combat class in combat.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class CombatClass {
    MELEE,
    RANGED,
    MAGIC;

    companion object {
        val values = enumValues<CombatClass>()
    }
}