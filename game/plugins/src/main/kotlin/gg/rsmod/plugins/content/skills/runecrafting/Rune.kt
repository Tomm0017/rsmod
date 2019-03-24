package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Triston Plummer ("Dread")
 *
 * @param id        The id of the rune that is to be crafted
 * @param essence   An array containing the valid essence ids
 * @param level     The level required to craft the rune
 * @param xp        The xp granted per crafted rune
 */
enum class Rune(val id: Int, val essence: IntArray = intArrayOf(Items.PURE_ESSENCE), val level: Int, val xp: Double) {

    AIR(id = Items.AIR_RUNE,        essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 1, xp = 5.0),
    MIND(id = Items.MIND_RUNE,      essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 2, xp = 5.5),
    WATER(id = Items.WATER_RUNE,    essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 5, xp = 6.0),
    EARTH(id = Items.EARTH_RUNE,    essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 9, xp = 6.5),
    FIRE(id = Items.FIRE_RUNE,      essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 14, xp = 7.0),
    BODY(id = Items.BODY_RUNE,      essence = intArrayOf(Items.RUNE_ESSENCE, Items.PURE_ESSENCE),   level = 20, xp = 7.5),
    COSMIC(id = Items.COSMIC_RUNE,                                                                  level = 27, xp = 8.0),
    CHAOS(id = Items.CHAOS_RUNE,                                                                    level = 35, xp = 8.5),
    ASTRAL(id = Items.ASTRAL_RUNE,                                                                  level = 40, xp = 8.7),
    NATURE(id = Items.NATURE_RUNE,                                                                  level = 44, xp = 9.0),
    LAW(id = Items.LAW_RUNE,                                                                        level = 54, xp = 9.5),
    DEATH(id = Items.DEATH_RUNE,                                                                    level = 65, xp = 10.0),
    BLOOD(id = Items.BLOOD_RUNE,    essence = intArrayOf(Items.DARK_ESSENCE_BLOCK),                 level = 77, xp = 23.8),
    SOUL(id = Items.SOUL_RUNE,      essence = intArrayOf(Items.DARK_ESSENCE_BLOCK),                 level = 90, xp = 29.7),
    WRATH(id = Items.WRATH_RUNE,                                                                    level = 95, xp = 8.0);

    /**
     * Gets the rune count multiplier for the player's Runecrafting level.
     * Taken from https://github.com/apollo-rsps/apollo/blob/kotlin-experiments/game/plugin/skills/runecrafting/src/Rune.kt
     *
     * @param level The runecrafting level
     *
     * @return  The number of runes per essence.
     */
    fun getBonusMultiplier(level: Int) : Double = when(this) {
        AIR -> (Math.floor((level / 11.0)) + 1)
        MIND -> (Math.floor((level / 14.0)) + 1)
        WATER -> (Math.floor((level / 19.0)) + 1)
        EARTH -> (Math.floor((level / 26.0)) + 1)
        FIRE -> (Math.floor((level / 35.0)) + 1)
        BODY -> (Math.floor((level / 46.0)) + 1)
        COSMIC -> (Math.floor((level / 59.0)) + 1)
        CHAOS -> (Math.floor((level / 74.0)) + 1)
        NATURE -> (Math.floor((level / 91.0)) + 1)
        else -> 1.0
    }

    companion object {
        val values = enumValues<Rune>()
    }
}