package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ProjectileType(val startHeight: Int, val endHeight: Int, val delay: Int,
                          val angle: Int, val steepness: Int) {
    BOLT(startHeight = 38, endHeight = 36, delay = 41, angle = 5, steepness = 11),
    SLOW_BOLT(startHeight = 38, endHeight = 36, delay = 41, angle = 5, steepness = 11),
    ARROW(startHeight = 40, endHeight = 36, delay = 41, angle = 15, steepness = 11),
    MAGIC_ARROW(startHeight = 40, endHeight = 36, delay = 31, angle = 15, steepness = 11),
    JAVELIN(startHeight = 38, endHeight = 36, delay = 42, angle = 1, steepness = 120),
    THROWN(startHeight = 40, endHeight = 36, delay = 32, angle = 15, steepness = 11),
    CHINCHOMPA(startHeight = 40, endHeight = 36, delay = 21, angle = 15, steepness = 11),
    MAGIC(startHeight = 43, endHeight = 31, delay = 51, angle = 16, steepness = 64);

    fun calculateLife(distance: Int): Int = when (this) {
        THROWN -> distance * 5
        CHINCHOMPA -> distance * 5
        MAGIC_ARROW, ARROW, BOLT -> Math.max(10, distance * 5)
        SLOW_BOLT -> distance*10
        JAVELIN -> (distance * 3) + 2
        /*
         * Handled in [gg.rsmod.plugins.content.combat.Combat.getProjectileLifespan].
         */
        MAGIC -> -1
    }
}