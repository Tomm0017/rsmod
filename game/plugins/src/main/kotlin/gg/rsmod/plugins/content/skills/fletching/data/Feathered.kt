package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

enum class Feathered(val id: Int, val unfeathered: Int, val amount: Int = 10, val feathersNeeded: Int = 1, val level: Int, val fletchingXP: Double) {
    // Headless (Ogre) Arrows (The only real exception here)
    HEADLESS_ARROW(id = Items.HEADLESS_ARROW, unfeathered = Items.ARROW_SHAFT, amount = 15, level = 1, fletchingXP = 1.0),
    FLIGHTED_OGRE_ARROW(id = Items.FLIGHTED_OGRE_ARROW, unfeathered = Items.OGRE_ARROW_SHAFT, amount = 6, feathersNeeded = 4, level = 1, fletchingXP = 0.9),

    // Bolts
    BRONZE_BOLTS(id = Items.BRONZE_BOLTS, unfeathered = Items.BRONZE_BOLTS_UNF, level = 9, fletchingXP = 0.5),
    BLURITE_BOLTS(id = Items.BLURITE_BOLTS, unfeathered = Items.BLURITE_BOLTS_UNF, level = 24, fletchingXP = 1.0),
    IRON_BOLTS(id = Items.IRON_BOLTS, unfeathered = Items.IRON_BOLTS_UNF, level = 39, fletchingXP = 1.5),
    SILVER_BOLTS(id = Items.SILVER_BOLTS, unfeathered = Items.SILVER_BOLTS_UNF, level = 43, fletchingXP = 2.5),
    STEEL_BOLTS(id = Items.STEEL_BOLTS, unfeathered = Items.STEEL_BOLTS_UNF, level = 46, fletchingXP = 3.5),
    MITHRIL_BOLTS(id = Items.MITHRIL_BOLTS, unfeathered = Items.MITHRIL_BOLTS_UNF, level = 54, fletchingXP = 5.0),
    BROAD_BOLTS(id = Items.BROAD_BOLTS, unfeathered = Items.UNFINISHED_BROAD_BOLTS, level = 55, fletchingXP = 3.0),
    ADAMANT_BOLTS(id = Items.ADAMANT_BOLTS, unfeathered = Items.ADAMANT_BOLTSUNF, level = 61, fletchingXP = 7.0),
    RUNITE_BOLTS(id = Items.RUNITE_BOLTS, unfeathered = Items.RUNITE_BOLTS_UNF, level = 69, fletchingXP = 10.0),
    DRAGON_BOLTS(id = Items.DRAGON_BOLTS, unfeathered = Items.DRAGON_BOLTS_UNF, level = 84, fletchingXP = 12.0),

    // Darts
    BRONZE_DART(id = Items.BRONZE_DART, unfeathered = Items.BRONZE_DART_TIP, level = 10, fletchingXP = 1.8),
    IRON_DART(id = Items.IRON_DART, unfeathered = Items.IRON_DART_TIP, level = 22, fletchingXP = 3.8),
    STEEL_DART(id = Items.STEEL_DART, unfeathered = Items.STEEL_DART_TIP, level = 37, fletchingXP = 7.5),
    MITHRIL_DART(id = Items.MITHRIL_DART, unfeathered = Items.MITHRIL_DART_TIP, level = 52, fletchingXP = 11.2),
    ADAMANT_DART(id = Items.ADAMANT_DART, unfeathered = Items.ADAMANT_DART_TIP, level = 67, fletchingXP = 15.0),
    RUNE_DART(id = Items.RUNE_DART, unfeathered = Items.RUNE_DART_TIP, level = 81, fletchingXP = 18.8),
    DRAGON_DART(id = Items.DRAGON_DART, unfeathered = Items.DRAGON_DART_TIP, level = 95, fletchingXP = 25.0);


    companion object {
        /**
         * The map of crossbow ids to its definition
         */
        val featheredDefinitions = values().associate { it.id to it}

        /**
         * The list of all possible feathers to be used
         */
        val feathers = setOf(
                Items.FEATHER,
                Items.RED_FEATHER,
                Items.ORANGE_FEATHER,
                Items.YELLOW_FEATHER,
                Items.BLUE_FEATHER,
                Items.STRIPY_FEATHER
        )
    }
}