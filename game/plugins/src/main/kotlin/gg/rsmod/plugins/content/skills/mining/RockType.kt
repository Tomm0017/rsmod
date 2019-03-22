package gg.rsmod.plugins.content.skills.mining

enum class RockType(val level: Int, val xp: Double, val ore: Int, val depleteChance: Int, val respawnTime: IntRange) {
    RUNE_ESSENCE(level = 1, xp = 13.0, ore = 1436, depleteChance = 0, respawnTime = 0..0),
    CLAY(level = 1, xp = 5.0, ore = 434, depleteChance = 1, respawnTime = 1..1),
    COPPER(level = 1, xp = 17.5, ore = 436, depleteChance = 0, respawnTime = 15..25),
    TIN(level = 1, xp = 17.5, ore = 438, depleteChance = 1, respawnTime = 15..25),
    BLURITE(level = 11, xp = 17.5, ore = 668, depleteChance = 1, respawnTime = 10..20),
    IRON(level = 16, xp = 35.0, ore = 440, depleteChance = 1, respawnTime = 1..1),
    SILVER(level = 21, xp = 40.0, ore = 442, depleteChance = 1, respawnTime = 1..1),
    ELEMENTAL(level = 21, xp = 0.0, ore = 2892, depleteChance = 1, respawnTime = 20..40),
    DAEYAL(level = 21, xp = 17.0, ore = 9632, depleteChance = 0, respawnTime = 25..45),
    COAL(level = 31, xp = 50.0, ore = 453, depleteChance = 0, respawnTime = 112..345),
    GOLD(level = 41, xp = 65.0, ore = 444, depleteChance = 1, respawnTime = 1..1),
    MITHRIL(level = 56, xp = 80.0, ore = 447, depleteChance = 1, respawnTime = 1..1),
    LOVAKITE(level = 66, xp = 10.0, ore = 13356, depleteChance = 0, respawnTime = 80..100),
    ADAMANTITE(level = 70, xp = 95.0, ore = 449, depleteChance = 1, respawnTime = 1..1),
    RUNITE(level = 85, xp = 125.0, ore = 451, depleteChance = 1, respawnTime = 100..150),
    AMETHYST(level = 92, xp = 240.0, ore = 21347, depleteChance = 1, respawnTime = 250..500);
}