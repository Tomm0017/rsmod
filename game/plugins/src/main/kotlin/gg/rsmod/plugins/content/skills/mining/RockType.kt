package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Misterbaho <MisterBaho#6447>
 */

enum class RockType(val level: Int, val xp: Double, val ore: Int, val depleteChance: Int, val respawnTime: Int) {
    CLAY(level = 1, xp = 5.0, ore = Items.CLAY, depleteChance = 0, respawnTime = 5),
    COPPER(level = 1, xp = 17.5, ore = Items.COPPER_ORE, depleteChance = 0, respawnTime = 5),
    TIN(level = 1, xp = 17.5, ore = Items.TIN_ORE, depleteChance = 0, respawnTime = 5),
    RUNITE(level = 85, xp = 125.0, ore = Items.RUNITE_ORE, depleteChance = 0, respawnTime = 720),
}