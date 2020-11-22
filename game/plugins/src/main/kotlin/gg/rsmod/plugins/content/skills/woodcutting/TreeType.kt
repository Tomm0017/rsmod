package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class TreeType(val level: Int, val xp: Double, val log: Int, val depleteChance: Int, val respawnTime: IntRange) {
    TREE(level = 1, xp = 25.0, log = Items.LOGS, depleteChance = 0, respawnTime = 15..25),
    ACHEY(level = 1, xp = 25.0, log = Items.ACHEY_TREE_LOGS, depleteChance = 0, respawnTime = 15..25),
    OAK(level = 15, xp = 37.5, log = Items.OAK_LOGS, depleteChance = 0, respawnTime = 15..25),
    WILLOW(level = 30, xp = 67.5, log = Items.WILLOW_LOGS, depleteChance = 8, respawnTime = 22..68),
    TEAK(level = 35, xp = 85.0, log = Items.TEAK_LOGS, depleteChance = 8, respawnTime = 22..68),
    MAPLE(level = 45, xp = 100.0, log = Items.MAPLE_LOGS, depleteChance = 8, respawnTime = 22..68),
    HOLLOW(level = 45, xp = 82.0, log = Items.BARK, depleteChance = 8, respawnTime = 22..68),
    MAHOGANY(level = 50, xp = 125.0, log = Items.MAHOGANY_LOGS, depleteChance = 8, respawnTime = 22..68),
    YEW(level = 60, xp = 175.0, log = Items.YEW_LOGS, depleteChance = 8, respawnTime = 22..68),
    MAGIC(level = 75, xp = 250.0, log = Items.MAGIC_LOGS, depleteChance = 8, respawnTime = 22..68),
    REDWOOD(level = 90, xp = 380.0, log = Items.REDWOOD_LOGS, depleteChance = 11, respawnTime = 50..100),
}