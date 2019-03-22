package gg.rsmod.plugins.content.skills.woodcutting

/**
 * @author Tom <rspsmods@gmail.com>
 *     & Lantern Web
 */
enum class TreeType(val level: Int, val xp: Double, val log: Int, val depleteChance: Int, val respawnTime: IntRange) {

    //TODO: petrate, cluerate, depletions, respawntimers
    TREE(level = 1, xp = 25.0, log = 1511, depleteChance = 1, respawnTime = 15..25),
    ACHEY(level = 1, xp = 25.0, log = 2862, depleteChance = 1, respawnTime = 15..25),
    OAK(level = 15, xp = 37.5, log = 1521, depleteChance = 2, respawnTime = 20..40),
    WILLOW(level = 30, xp = 67.5, log = 1519, depleteChance = 8, respawnTime = 15..20),
    TEAK(level = 35, xp = 85.0, log = 6333, depleteChance = 11, respawnTime = 22..55),
    MAPLE(level = 45, xp = 100.0, log = 1517, depleteChance = 11, respawnTime = 30..50),
    HOLLOW(level = 45, xp = 82.0, log = 3239, depleteChance = 8, respawnTime = 22..68),
    MAHOGANY(level = 50, xp = 125.0, log = 6332, depleteChance = 11, respawnTime = 22..68),
    YEW(level = 60, xp = 175.0, log = 1515, depleteChance = 14, respawnTime = 30..60),
    MAGIC(level = 75, xp = 250.0, log = 1513, depleteChance = 18, respawnTime = 120..120),
    REDWOOD(level = 90, xp = 380.0, log = 19669, depleteChance = 11, respawnTime = 50..100),

}