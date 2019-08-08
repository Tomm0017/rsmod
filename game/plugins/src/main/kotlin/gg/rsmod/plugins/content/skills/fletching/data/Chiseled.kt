package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

enum class Chiseled(val id: Int, val unchiseled: Int, val amount: Int = 12, val level: Int, val animation: Int, val fletchingXP: Double) {
    // TODO: FIND ANIMATION IDS!!!
    // Bolt tips
    OPAL_BOLT_TIPS(id = Items.OPAL_BOLT_TIPS, unchiseled = Items.OPAL, level = 11, animation = 890, fletchingXP = 1.6),
    JADE_BOLT_TIPS(id = Items.JADE_BOLT_TIPS, unchiseled = Items.JADE, level = 26, animation = 891, fletchingXP = 2.4),
    PEARL_BOLT_TIPS_6(id = Items.PEARL_BOLT_TIPS, unchiseled = Items.OYSTER_PEARL, level = 41, amount = 6, animation = 4470, fletchingXP = 3.2),
    PEARL_BOLT_TIPS_24(id = Items.PEARL_BOLT_TIPS, unchiseled = Items.OYSTER_PEARLS, level = 41, amount = 24, animation = 4470, fletchingXP = 3.2),
    TOPAZ_BOLT_TIPS(id = Items.TOPAZ_BOLT_TIPS, unchiseled = Items.RED_TOPAZ, level = 48, animation = 892, fletchingXP = 3.9),
    SAPPHIRE_BOLT_TIPS(id = Items.SAPPHIRE_BOLT_TIPS, unchiseled = Items.SAPPHIRE, level = 56, animation = 888, fletchingXP = 4.7),
    EMERALD_BOLT_TIPS(id = Items.EMERALD_BOLT_TIPS, unchiseled = Items.EMERALD, level = 58, animation = 889, fletchingXP = 5.5),
    RUBY_BOLT_TIPS(id = Items.RUBY_BOLT_TIPS, unchiseled = Items.RUBY, level = 63, animation = 887, fletchingXP = 6.3),
    DIAMOND_BOLT_TIPS(id = Items.DIAMOND_BOLT_TIPS, unchiseled = Items.DIAMOND, level = 65, animation = 886, fletchingXP = 7.0),
    DRAGONSTONE_BOLT_TIPS(id = Items.DRAGONSTONE_BOLT_TIPS, unchiseled = Items.DRAGONSTONE, level = 71, animation = 885, fletchingXP = 8.2),
    ONYX_BOLT_TIPS(id = Items.ONYX_BOLT_TIPS, unchiseled = Items.ONYX, level = 73, animation = 885, fletchingXP = 9.4),

    // Kebbit Bolts
    KEBBIT_BOLTS(id = Items.KEBBIT_BOLTS, unchiseled = Items.KEBBIT_SPIKE, level = 32, amount = 6, animation = 885, fletchingXP = 5.8),
    LONG_KEBBIT_BOLTS(id = Items.LONG_KEBBIT_BOLTS, unchiseled = Items.LONG_KEBBIT_SPIKE, level = 42, amount = 6, animation = 885, fletchingXP = 7.9);

    companion object {
        /**
         * The map of chiseled ids to its definition
         */
        val chiseledDefinitions = values().associate { it.id to it}
    }
}