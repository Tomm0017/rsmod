package gg.rsmod.plugins.content.skills.woodcutting

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class TreeType(val level: Int, val xp: Double, val log: Int, val depleteChance: Int, val respawnTime: IntRange) {

    // TODO(Tom): fill data
    TREE(level = 1, xp = 25.0, log = 1511, depleteChance = 0, respawnTime = 15..25),
    REDWOOD(level = 90, xp = 380.0, log = 19669, depleteChance = 11, respawnTime = 50..100)
}