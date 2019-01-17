package gg.rsmod.plugins.osrs.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class HitbarType(val id: Int, val percentage: Int) {
    NORMAL(id = 0, percentage = 30),
    MEDIUM(id = 5, percentage = 60),
    LARGE(id = 1, percentage = 100),
    LARGE_1X(id = 2, percentage = 120),
    LARGE_2X(id = 3, percentage = 140),
    LARGE_3X(id = 3, percentage = 160),
    BLUE(id = 7, percentage = 100),
    ORANGE(id = 8, percentage = 120)
}