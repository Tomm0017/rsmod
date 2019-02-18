package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class HitbarType(val id: Int, val pixelsWide: Int) {
    NORMAL(id = 0, pixelsWide = 30),
    MEDIUM(id = 5, pixelsWide = 60),
    LARGE(id = 1, pixelsWide = 100),
    LARGE_1X(id = 2, pixelsWide = 120),
    LARGE_2X(id = 3, pixelsWide = 140),
    LARGE_3X(id = 3, pixelsWide = 160),
    BLUE(id = 7, pixelsWide = 100),
    ORANGE(id = 8, pixelsWide = 120)
}