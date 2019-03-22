package gg.rsmod.plugins.content.skills.woodcutting

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class TreeType(val level: Int, val xp: Double, val log: Int, val depleteChance: Int, val respawnTime: IntRange) {
    TREE(level = 1, xp = 25.0, log = 1511, depleteChance = 0, respawnTime = 15..25),
    ACHEY(level = 1, xp = 25.0, log = 2862, depleteChance = 0, respawnTime = 15..25),
    OAK(level = 15, xp = 37.5, log = 1521, depleteChance = 0, respawnTime = 15..25),
    WILLOW(level = 30, xp = 67.5, log = 1519, depleteChance = 8, respawnTime = 22..68),
    TEAK(level = 35, xp = 85.0, log = 6333, depleteChance = 8, respawnTime = 22..68),
    MAPLE(level = 45, xp = 100.0, log = 1517, depleteChance = 8, respawnTime = 22..68),
    HOLLOW(level = 45, xp = 82.0, log = 3239, depleteChance = 8, respawnTime = 22..68),
    MAHOGANY(level = 50, xp = 125.0, log = 6332, depleteChance = 8, respawnTime = 22..68),
    YEW(level = 60, xp = 175.0, log = 1515, depleteChance = 8, respawnTime = 22..68),
    MAGIC(level = 75, xp = 250.0, log = 1513, depleteChance = 8, respawnTime = 22..68),
    REDWOOD(level = 90, xp = 380.0, log = 19669, depleteChance = 11, respawnTime = 50..100),
}