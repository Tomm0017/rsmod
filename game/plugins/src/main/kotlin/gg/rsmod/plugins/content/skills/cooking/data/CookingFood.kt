package gg.rsmod.plugins.content.skills.cooking.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Kyle Escobar
 *
 * Represents the foods that can be cooked.
 *
 * @param raw_item          The [Item.id] of the raw food
 * @param cooked_item       The [Item.id] of the cooked food
 * @param burnt_item        The [Item.id] of the burnt food
 * @param minLevel          The minimum level to cook the food.
 * @param maxLevel          The level which food no longer will burn.
 * @param xp                The amount of xp gained when successfully cooking the food.
 * @param requireRange      Boolean whether or not this food can be cooked on fires.
 */
enum class CookingFood(val raw_item: Int, val cooked_item: Int, val burnt_item: Int, val xp: Double, val minLevel: Int, val maxLevel: Int, val requireRange: Boolean = true) {
    SHRIMP(Items.RAW_SHRIMPS, Items.SHRIMPS, Items.BURNT_SHRIMP, 30.0, 1, 34, false),
    CHICKEN(Items.RAW_CHICKEN, Items.COOKED_CHICKEN, Items.BURNT_CHICKEN, 30.0, 1, 34, false),
    BEEF(Items.RAW_BEEF, Items.COOKED_MEAT, Items.BURNT_MEAT, 30.0, 1, 31, false),
    RAT_MEAT(Items.RAW_RAT_MEAT, Items.COOKED_MEAT, Items.BURNT_MEAT, 30.0, 1, 31, false),
    SARDINE(Items.RAW_SARDINE, Items.SARDINE, Items.BURNT_FISH_369, 40.0, 1, 35, false),
    HERRING(Items.RAW_HERRING, Items.HERRING, Items.BURNT_FISH_357, 50.0, 5, 41, false),
    MACKEREL(Items.RAW_MACKEREL, Items.MACKEREL, Items.BURNT_FISH_357, 40.0, 10, 45, false),
    TROUT(Items.RAW_TROUT, Items.TROUT, Items.BURNT_FISH_343, 70.0, 15, 50,false),
    ANCHOVIES(Items.RAW_ANCHOVIES, Items.ANCHOVIES, Items.BURNT_FISH, 30.0, 15, 34, false),
    COD(Items.RAW_COD, Items.COD, Items.BURNT_FISH_343, 75.0, 18, 52, false),
    PIKE(Items.RAW_PIKE, Items.PIKE, Items.BURNT_FISH_343, 80.0, 20, 64, false),
    ROAST_BEAST(Items.RAW_BEAST_MEAT, Items.ROAST_BEAST_MEAT, Items.BURNT_BEAST_MEAT, 82.5, 21, 99, false),
    SALMON(Items.RAW_SALMON, Items.SALMON, Items.BURNT_FISH_343, 90.0, 25, 58,false),
    TUNA(Items.RAW_TUNA, Items.TUNA, Items.BURNT_FISH_367, 100.0, 30, 64, false),
    RAINBOW_FISH(Items.RAW_RAINBOW_FISH, Items.RAINBOW_FISH, Items.BURNT_RAINBOW_FISH, 110.0, 35, 63, false),
    STEW(Items.UNCOOKED_STEW, Items.STEW, Items.BURNT_STEW, 117.0, 25, 58, true),
    CAKE(Items.UNCOOKED_CAKE, Items.CAKE, Items.BURNT_CAKE, 180.0, 40, 99, true),
    MEAT_PIE(Items.UNCOOKED_MEAT_PIE, Items.MEAT_PIE, Items.BURNT_PIE, 110.0, 20, 99, true),
    LOBSTER(Items.RAW_LOBSTER, Items.LOBSTER, Items.BURNT_LOBSTER, 90.0, 40, 74, false),
    BASS(Items.RAW_BASS, Items.BASS, Items.BURNT_FISH_367, 130.0, 43, 80, false),
    SWORDFISH(Items.RAW_SWORDFISH, Items.SWORDFISH, Items.BURNT_SWORDFISH, 140.0, 45, 86, false),
    BAKED_POTATO(Items.POTATO, Items.BAKED_POTATO, Items.BURNT_POTATO, 15.0, 7, 50, false),
    MONKFISH(Items.RAW_MONKFISH, Items.MONKFISH, Items.BURNT_MONKFISH, 150.0, 62, 92, false),
    KARAMBWAN(Items.RAW_KARAMBWAN, Items.COOKED_KARAMBWAN, Items.BURNT_KARAMBWAN, 190.0, 30, 99, false),
    SHARK(Items.RAW_SHARK, Items.SHARK, Items.BURNT_SHARK, 210.0, 80, 99, false),
    SEA_TURTLE(Items.RAW_SEA_TURTLE, Items.SEA_TURTLE, Items.BURNT_SEA_TURTLE, 211.3, 82, 99, false),
    MANTA_RAY(Items.RAW_MANTA_RAY, Items.MANTA_RAY, Items.BURNT_MANTA_RAY, 216.3, 91, 99, false),
    DARK_CRAB(Items.RAW_DARK_CRAB, Items.DARK_CRAB, Items.BURNT_DARK_CRAB, 215.0, 90, 99, false),
    ANGLERFISH(Items.RAW_ANGLERFISH, Items.ANGLERFISH, Items.BURNT_ANGLERFISH, 230.0, 84, 99, false),
    BREAD(Items.BREAD_DOUGH, Items.BREAD, Items.BURNT_BREAD, 40.0, 1, 34, true);

    companion object {
        val values = enumValues<CookingFood>()

        val definitions = values.associate { it.raw_item to it }
    }
}