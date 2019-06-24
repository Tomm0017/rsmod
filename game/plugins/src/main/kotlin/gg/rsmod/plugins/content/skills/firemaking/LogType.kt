package gg.rsmod.plugins.content.skills.firemaking

/**
 * Represents all type of logs that can be interacted with (e.g. burned) through the [Firemaking] skill.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-06-23
 * @version 1.0
 *
 * @param level the fire-making level required to burn this type of log
 * @param xp the experience gained from burning one log of this type
 * @param burnTime represents the range of possible durations for which a log of this type burns
 *                  (this is currently the same as the respawnTime time for corresponding tree types)
 */
enum class LogType(val level: Int, val xp: Double, val burnTime: IntRange) {
    NORMAL(level = 1, xp = 40.0, burnTime = 15..25),
    ACHEY(level = 1, xp = 40.0, burnTime = 15..25),
    OAK(level = 15, xp = 69.5, burnTime = 15..25),
    WILLOW(level = 30, xp = 90.0, burnTime = 22..68),
    TEAK(level = 35, xp = 105.0, burnTime = 22..68),
    ARCTIC_PINE(level = 42, xp = 125.0, burnTime = 22..68),
    MAPLE(level = 45, xp = 135.0, burnTime = 22..68),
    MAHOGANY(level = 50, xp = 157.5, burnTime = 22..68),
    YEW(level = 60, xp = 202.5, burnTime = 22..68),
    MAGIC(level = 75, xp = 303.8, burnTime = 22..68),
    REDWOOD(level = 90, xp = 350.0, burnTime = 50..100)
}