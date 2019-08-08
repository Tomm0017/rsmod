package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

enum class Tipped(val id: Int, val base: Int, val tip: Int, val level: Int, val setAmount: Int, val fletchingXP: Double) {
    // Arrows
    BRONZE_ARROW(id = Items.BRONZE_ARROW, base = Items.HEADLESS_ARROW, tip = Items.BRONZE_ARROWTIPS, level = 1, setAmount = 15, fletchingXP = 1.3),
    IRON_ARROW(id = Items.IRON_ARROW, base = Items.HEADLESS_ARROW, tip = Items.IRON_ARROWTIPS, level = 15, setAmount = 15, fletchingXP = 2.5),
    STEEL_ARROW(id = Items.STEEL_ARROW, base = Items.HEADLESS_ARROW, tip = Items.STEEL_ARROWTIPS, level = 30, setAmount = 15, fletchingXP = 5.0),
    MITHRIL_ARROW(id = Items.MITHRIL_ARROW, base = Items.HEADLESS_ARROW, tip = Items.MITHRIL_ARROWTIPS, level = 45, setAmount = 15, fletchingXP = 7.5),
    BROAD_ARROW(id = Items.BROAD_ARROWS, base = Items.HEADLESS_ARROW, tip = Items.BROAD_ARROWHEADS, level = 52, setAmount = 15, fletchingXP = 10.0),
    ADAMANT_ARROW(id = Items.ADAMANT_ARROW, base = Items.HEADLESS_ARROW, tip = Items.ADAMANT_ARROWTIPS, level = 60, setAmount = 15, fletchingXP = 10.0),
    RUNE_ARROW(id = Items.RUNE_ARROW, base = Items.HEADLESS_ARROW, tip = Items.RUNE_ARROWTIPS, level = 75, setAmount = 15, fletchingXP = 12.5),
    AMETHYST_ARROW(id = Items.AMETHYST_ARROW, base = Items.HEADLESS_ARROW, tip = Items.AMETHYST_ARROWTIPS, level = 82, setAmount = 15, fletchingXP = 13.5),
    DRAGON_ARROW(id = Items.DRAGON_ARROW, base = Items.HEADLESS_ARROW, tip = Items.DRAGON_ARROWTIPS, level = 90, setAmount = 15, fletchingXP = 15.0),

    // Ogre Arrows
    OGRE_ARROW(id = Items.OGRE_ARROW, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.WOLFBONE_ARROWTIPS, level = 5, setAmount = 6, fletchingXP = 1.0),
    BRONZE_BRUTAL(id = Items.BRONZE_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.BRONZE_NAILS, level = 7, setAmount = 6, fletchingXP = 1.4),
    IRON_BRUTAL(id = Items.IRON_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.IRON_NAILS, level = 18, setAmount = 6, fletchingXP = 2.6),
    STEEL_BRUTAL(id = Items.STEEL_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.STEEL_NAILS, level = 33, setAmount = 6, fletchingXP = 5.1),
    BLACK_BRUTAL(id = Items.BLACK_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.BLACK_NAILS, level = 38, setAmount = 6, fletchingXP = 6.5),
    MITHRIL_BRUTAL(id = Items.MITHRIL_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.MITHRIL_NAILS, level = 49, setAmount = 6, fletchingXP = 7.5),
    ADAMANT_BRUTAL(id = Items.ADAMANT_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.ADAMANTITE_NAILS, level = 62, setAmount = 6, fletchingXP = 10.2),
    RUNE_BRUTAL(id = Items.RUNE_BRUTAL, base = Items.FLIGHTED_OGRE_ARROW, tip = Items.RUNE_NAILS, level = 77, setAmount = 6, fletchingXP = 12.5),

    // Bolts
    OPAL_BOLTS(id = Items.OPAL_BOLTS, base = Items.BRONZE_BOLTS, tip = Items.OPAL_BOLT_TIPS, level = 11, setAmount = 10, fletchingXP = 1.6),
    JADE_BOLTS(id = Items.JADE_BOLTS, base = Items.BLURITE_BOLTS, tip = Items.JADE_BOLT_TIPS, level = 26, setAmount = 10, fletchingXP = 2.4),
    PEARL_BOLTS(id = Items.PEARL_BOLTS, base = Items.IRON_BOLTS, tip = Items.PEARL_BOLT_TIPS, level = 41, setAmount = 10, fletchingXP = 3.2),
    TOPAZ_BOLTS(id = Items.TOPAZ_BOLTS, base = Items.STEEL_BOLTS, tip = Items.TOPAZ_BOLT_TIPS, level = 48, setAmount = 10, fletchingXP = 3.9),
    BARBED_BOLTS(id = Items.BARBED_BOLTS, base = Items.BRONZE_BOLTS, tip = Items.BARB_BOLTTIPS, level = 51, setAmount = 10, fletchingXP = 9.5),
    SAPPHIRE_BOLTS(id = Items.SAPPHIRE_BOLTS, base = Items.MITHRIL_BOLTS, tip = Items.SAPPHIRE_BOLT_TIPS, level = 56, setAmount = 10, fletchingXP = 4.7),
    EMERALD_BOLTS(id = Items.EMERALD_BOLTS, base = Items.MITHRIL_BOLTS, tip = Items.EMERALD_BOLT_TIPS, level = 58, setAmount = 10, fletchingXP = 5.5),
    RUBY_BOLTS(id = Items.RUBY_BOLTS, base = Items.ADAMANT_BOLTS, tip = Items.RUBY_BOLT_TIPS, level = 63, setAmount = 10, fletchingXP = 6.3),
    DIAMOND_BOLTS(id = Items.DIAMOND_BOLTS, base = Items.ADAMANT_BOLTS, tip = Items.DIAMOND_BOLT_TIPS, level = 65, setAmount = 10, fletchingXP = 7.0),
    DRAGONSTONE_BOLTS(id = Items.DRAGONSTONE_BOLTS, base = Items.RUNITE_BOLTS, tip = Items.DRAGONSTONE_BOLT_TIPS, level = 71, setAmount = 10, fletchingXP = 8.2),
    ONYX_BOLTS(id = Items.ONYX_BOLTS, base = Items.RUNITE_BOLTS, tip = Items.ONYX_BOLT_TIPS, level = 73, setAmount = 10, fletchingXP = 9.4),
    AMETHYST_BROAD_BOLTS(id = Items.AMETHYST_BROAD_BOLTS, base = Items.BROAD_BOLTS, tip = Items.AMETHYST_BOLT_TIPS, level = 76, setAmount = 10, fletchingXP = 10.6),

    // Dragon Bolts
    OPAL_DRAGON_BOLTS(id = Items.OPAL_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.OPAL_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 1.6),
    JADE_DRAGON_BOLTS(id = Items.JADE_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.JADE_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 2.4),
    PEARL_DRAGON_BOLTS(id = Items.PEARL_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.PEARL_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 3.2),
    TOPAZ_DRAGON_BOLTS(id = Items.TOPAZ_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.TOPAZ_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 3.9),
    SAPPHIRE_DRAGON_BOLTS(id = Items.SAPPHIRE_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.SAPPHIRE_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 4.7),
    EMERALD_DRAGON_BOLTS(id = Items.EMERALD_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.EMERALD_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 5.5),
    RUBY_DRAGON_BOLTS(id = Items.RUBY_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.RUBY_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 6.3),
    DIAMOND_DRAGON_BOLTS(id = Items.DIAMOND_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.DIAMOND_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 7.0),
    DRAGONSTONE_DRAGON_BOLTS(id = Items.DRAGONSTONE_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.DRAGONSTONE_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 8.2),
    ONYX_DRAGON_BOLTS(id = Items.ONYX_DRAGON_BOLTS, base = Items.DRAGON_BOLTS, tip = Items.ONYX_BOLT_TIPS, level = 84, setAmount = 10, fletchingXP = 9.4),

    // Javelins
    BRONZE_JAVELIN(id = Items.BRONZE_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.BRONZE_JAVELIN_HEADS, level = 3, setAmount = 15, fletchingXP = 1.0),
    IRON_JAVELIN(id = Items.IRON_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.IRON_JAVELIN_HEADS, level = 17, setAmount = 15, fletchingXP = 2.0),
    STEEL_JAVELIN(id = Items.STEEL_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.STEEL_JAVELIN_HEADS, level = 32, setAmount = 15, fletchingXP = 5.0),
    MITHRIL_JAVELIN(id = Items.MITHRIL_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.MITHRIL_JAVELIN_HEADS, level = 47, setAmount = 15, fletchingXP = 8.0),
    ADAMANT_JAVELIN(id = Items.ADAMANT_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.ADAMANT_JAVELIN_HEADS, level = 62, setAmount = 15, fletchingXP = 10.0),
    RUNE_JAVELIN(id = Items.RUNE_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.RUNE_JAVELIN_HEADS, level = 77, setAmount = 15, fletchingXP = 12.4),
    AMETHYST_JAVELIN(id = Items.AMETHYST_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.AMETHYST_JAVELIN_HEADS, level = 84, setAmount = 15, fletchingXP = 13.5),
    DRAGON_JAVELIN(id = Items.DRAGON_JAVELIN, base = Items.JAVELIN_SHAFT, tip = Items.DRAGON_JAVELIN_HEADS, level = 92, setAmount = 15, fletchingXP = 15.0);


    companion object {
        /**
         * The map of tipped ids to its definition
         */
        val tippedDefinitions = values().associate { it.id to it}

    }
}