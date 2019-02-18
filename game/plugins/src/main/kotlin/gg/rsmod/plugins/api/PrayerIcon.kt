package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class PrayerIcon(val id: Int) {
    NONE(id = -1),
    PROTECT_FROM_MELEE(id = 0),
    PROTECT_FROM_MISSILES(id = 1),
    PROTECT_FROM_MAGIC(id = 2),
    RETRIBUTION(id = 3),
    SMITE(id = 4),
    REDEMPTION(id = 5)
}