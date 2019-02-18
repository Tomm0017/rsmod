package gg.rsmod.plugins.content.items.food

import gg.rsmod.plugins.api.cfg.Items

enum class Food(val item: Int, val heal: Int = 0, val overheal: Boolean = false,
                val replacement: Int = -1, val tickDelay: Int = 3,
                val comboFood: Boolean = false) {

    /**
     * Sea food.
     */
    SHRIMP(item = Items.SHRIMPS, heal = 3),
    SARDINE(item = Items.SARDINE, heal = 4),
    HERRING(item = Items.HERRING, heal = 5),
    MACKEREL(item = Items.MACKEREL, heal = 6),
    TROUT(item = Items.TROUT, heal = 7),
    SALMON(item = Items.SALMON, heal = 9),
    TUNA(item = Items.TUNA, heal = 10),
    LOBSTER(item = Items.LOBSTER, heal = 12),
    BASS(item = Items.BASS, heal = 13),
    SWORDFISH(item = Items.SWORDFISH, heal = 14),
    MONKFISH(item = Items.MONKFISH, heal = 16),
    KARAMBWAN(item = Items.COOKED_KARAMBWAN, heal = 18, comboFood = true),
    SHARK(item = Items.SHARK, heal = 20),
    MANTA_RAY(item = Items.MANTA_RAY, heal = 21),
    DARK_CRAB(item = Items.DARK_CRAB, heal = 22),
    ANGLERFISH(item = Items.ANGLERFISH, overheal = true),

    /**
     * Meat.
     */
    CHICKEN(item = Items.COOKED_CHICKEN, heal = 4),
    MEAT(item = Items.COOKED_MEAT, heal = 4),

    /**
     * Pastries.
     */
    BREAD(item = Items.BREAD, heal = 5);

    companion object {
        val values = enumValues<Food>()
    }
}