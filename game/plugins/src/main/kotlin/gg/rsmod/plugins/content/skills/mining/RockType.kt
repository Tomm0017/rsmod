package gg.rsmod.plugins.content.skills.mining

enum class RockType(val level: Int, val xp: Double, val ore: Int, val depleteChance: Int, val respawnTime: IntRange) {
    CLAY(level = 1, xp = 5.0, ore = 1, depleteChance = 1, respawnTime = 1..1),
    COPPER(level = 1, xp = 17.5, ore = 1511, depleteChance = 0, respawnTime = 15..25),
    TIN(level = 1, xp = 17.5, ore = 1, depleteChance = 1, respawnTime = 1..1),
    IRON(level = 16, xp = 35.0, ore = 1, depleteChance = 1, respawnTime = 1..1),
    SILVER(level = 21, xp = 40.0, ore = 1, depleteChance = 1, respawnTime = 1..1 ),
    COAL(level = 31, xp = 50.0, ore = 3243, depleteChance = 0, respawnTime = 112..345),
    GOLD(level = 41, xp = 65.0, ore = 1, depleteChance = 1, respawnTime = 1..1 ),
    MITHRIL(level = 56, xp = 80.0, ore =1 , depleteChance = 1, respawnTime = 1..1 ),
    ADAMANTITE(level = 70, xp = 95.0, ore = 1, depleteChance = 1, respawnTime = 1..1 ),
    RUNITE(level = 85, xp = 125.0, ore = 1, depleteChance = 1, respawnTime = 1..1 );

}