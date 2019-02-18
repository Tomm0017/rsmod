package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class HitType(val id: Int) {
    BLOCK(id = 0),
    HIT(id = 1),
    POISON(id = 2),
    YELLOW(id = 3), //NOTE: find real use for this and name it accordingly
    DISEASE(id = 4),
    VENOM(id = 5),
    HEAL(id = 6);
}