package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class BonusSlot(val id: Int) {
    ATTACK_STAB(id = 0),
    ATTACK_SLASH(id = 1),
    ATTACK_CRUSH(id = 2),
    ATTACK_MAGIC(id = 3),
    ATTACK_RANGED(id = 4),

    DEFENCE_STAB(id = 5),
    DEFENCE_SLASH(id = 6),
    DEFENCE_CRUSH(id = 7),
    DEFENCE_MAGIC(id = 8),
    DEFENCE_RANGED(id = 9)
}