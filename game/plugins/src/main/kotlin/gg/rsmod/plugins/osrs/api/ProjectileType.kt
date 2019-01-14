package gg.rsmod.plugins.osrs.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ProjectileType(val startHeight: Int, val endHeight: Int, val delay: Int,
                          val angle: Int, val steepness: Int) {
    BOLT(startHeight = 38, endHeight = 36, delay = 41, angle = 5, steepness = 11),
    ARROW(startHeight = 40, endHeight = 36, delay = 41, angle = 15, steepness = 11),
    JAVELIN(startHeight = 38, endHeight = 36, delay = 42, angle = 1, steepness = 120);

    fun calculateLife(distance: Int): Int = when (this) {
        BOLT -> Math.max(10, distance * 5)
        ARROW -> Math.max(10, distance * 5)
        JAVELIN -> (distance * 3) + 2
    }
}