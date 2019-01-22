package gg.rsmod.game.model.combat

/**
 * Represents a valid attack style in combat.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class AttackStyle(val id: Int) {
    NONE(id = -1),
    ACCURATE(id = 0),
    AGGRESSIVE(id = 1),
    DEFENSIVE(id = 2),
    CONTROLLED(id = 3),
    RAPID(id = 4),
    LONG_RANGE(id = 5)
}