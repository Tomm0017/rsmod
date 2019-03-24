package gg.rsmod.plugins.content.skills.fishing

/**
 * @author Lantern Web
 */
enum class FishingSpotType(val level: Int, val xp: Double, val bait: Int, val depleteChance: Int, val respawnTime: IntRange) {

    //TODO: petrate, cluerate, depletions, respawntimers
    NET(level = 1, xp = 25.0, bait = 1511, depleteChance = 1, respawnTime = 15..25),


}