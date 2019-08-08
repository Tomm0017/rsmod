package gg.rsmod.plugins.content.skills.cooking.data

import gg.rsmod.plugins.api.cfg.Items

enum class CookingIngredient(val item1: Int, val item2: Int, val usedItem1: Int, val usedItem2: Int, val result: Int, val minLevel: Int, val xp: Double) {

    BREAD_DOUGH(Items.POT_OF_FLOUR, Items.BUCKET_OF_WATER, Items.POT, Items.EMPTY_BUCKET, Items.BREAD_DOUGH, 1,0.0),
    PASTRY_DOUGH(Items.POT_OF_FLOUR, Items.BUCKET_OF_WATER, Items.POT, Items.EMPTY_BUCKET, Items.PASTRY_DOUGH, 1, 0.0),
    PIZZA_BASE(Items.POT_OF_FLOUR, Items.BUCKET_OF_WATER, Items.POT, Items.EMPTY_BUCKET, Items.PIZZA_BASE, 35, 0.0),
    INCOMPLETE_STEW(Items.BOWL_OF_WATER, Items.POTATO, -1, -1, Items.INCOMPLETE_STEW, 25, 0.0),
    UNCOOKED_STEW(Items.INCOMPLETE_STEW, Items.COOKED_MEAT, -1, -1, Items.UNCOOKED_STEW, 25, 0.0),
    UNCOOKED_STEW_ALT(Items.INCOMPLETE_STEW, Items.COOKED_CHICKEN, -1, -1, Items.UNCOOKED_STEW, 25, 0.0),
    PIE_SHELL(Items.PASTRY_DOUGH, Items.PIE_DISH, -1, -1, Items.PIE_SHELL, 20, 0.0),
    UNCOOKED_MEAT_PIE(Items.PIE_SHELL, Items.COOKED_MEAT, -1, -1, Items.UNCOOKED_MEAT_PIE, 20, 0.0),
    POTATO_BUTTER(Items.BAKED_POTATO, Items.PAT_OF_BUTTER, -1, -1, Items.POTATO_WITH_BUTTER, 39, 40.0);

    companion object {
        val values = enumValues<CookingIngredient>()
    }
}