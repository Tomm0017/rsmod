package gg.rsmod.game.model.combat

/**
 * Represents a valid combat style.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class CombatStyle(val id: Int) {
    NONE(id = -1),
    STAB(id = 0),
    SLASH(id = 1),
    CRUSH(id = 2),
    MAGIC(id = 3),
    RANGED(id = 4)
}