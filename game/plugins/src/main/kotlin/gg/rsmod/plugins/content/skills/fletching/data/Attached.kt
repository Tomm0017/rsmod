package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

enum class Attached(val id: Int, val toolRequired: Int = -1, val firstMaterial: Int, val secondMaterial: Int, val level: Int, val animation: Int, val fletchingXP: Double) {
    // Bow Stringing
    SHORTBOW(id = Items.SHORTBOW, firstMaterial = Items.SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 5, animation = 6678, fletchingXP = 5.0),
    LONGBOW(id = Items.LONGBOW, firstMaterial = Items.LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 10, animation = 6684, fletchingXP = 10.0),
    OAK_SHORTBOW(id = Items.OAK_SHORTBOW, firstMaterial = Items.OAK_SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 20, animation = 6679, fletchingXP = 16.5),
    OAK_LONGBOW(id = Items.OAK_LONGBOW, firstMaterial = Items.OAK_LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 25, animation = 6685, fletchingXP = 25.0),
    COMP_OGRE_BOW(id = Items.COMP_OGRE_BOW, firstMaterial = Items.UNSTRUNG_COMP_BOW, secondMaterial = Items.BOW_STRING, level = 30, animation = 6686, fletchingXP = 45.0),
    WILLOW_SHORTBOW(id = Items.WILLOW_SHORTBOW, firstMaterial = Items.WILLOW_SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 35, animation = 6680, fletchingXP = 33.3),
    WILLOW_LONGBOW(id = Items.WILLOW_LONGBOW, firstMaterial = Items.WILLOW_LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 40, animation = 6686, fletchingXP = 41.5),
    MAPLE_SHORTBOW(id = Items.MAPLE_SHORTBOW, firstMaterial = Items.MAPLE_SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 50, animation = 6681, fletchingXP = 50.0),
    MAPLE_LONGBOW(id = Items.MAPLE_LONGBOW, firstMaterial = Items.MAPLE_LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 55, animation = 6687, fletchingXP = 58.3),
    YEW_SHORTBOW(id = Items.YEW_SHORTBOW, firstMaterial = Items.YEW_SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 65, animation = 6682, fletchingXP = 67.5),
    YEW_LONGBOW(id = Items.YEW_LONGBOW, firstMaterial = Items.YEW_LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 70, animation = 6688, fletchingXP = 75.0),
    MAGIC_SHORTBOW(id = Items.MAGIC_SHORTBOW, firstMaterial = Items.MAGIC_SHORTBOW_U, secondMaterial = Items.BOW_STRING, level = 80, animation = 6683, fletchingXP = 83.3),
    MAGIC_LONGBOW(id = Items.MAGIC_LONGBOW, firstMaterial = Items.MAGIC_LONGBOW_U, secondMaterial = Items.BOW_STRING, level = 85, animation = 6689, fletchingXP = 91.5),

    // Crossbow stringing
    BRONZE_CROSSBOW(id = Items.BRONZE_CROSSBOW, firstMaterial = Items.BRONZE_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 9, animation = 6671, fletchingXP = 6.0),
    BLURITE_CROSSBOW(id = Items.BLURITE_CROSSBOW, firstMaterial = Items.BLURITE_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 24, animation = 6672, fletchingXP = 16.0),
    IRON_CROSSBOW(id = Items.IRON_CROSSBOW, firstMaterial = Items.IRON_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 39, animation = 6673, fletchingXP = 22.0),
    STEEL_CROSSBOW(id = Items.STEEL_CROSSBOW, firstMaterial = Items.STEEL_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 46, animation = 6674, fletchingXP = 27.0),
    MITH_CROSSBOW(id = Items.MITH_CROSSBOW, firstMaterial = Items.MITHRIL_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 54, animation = 6675, fletchingXP = 32.0),
    ADAMANT_CROSSBOW(id = Items.ADAMANT_CROSSBOW, firstMaterial = Items.ADAMANT_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 61, animation = 6676, fletchingXP = 41.0),
    RUNE_CROSSBOW(id = Items.RUNE_CROSSBOW, firstMaterial = Items.RUNITE_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 69, animation = 6677, fletchingXP = 50.0),
    DRAGON_CROSSBOW(id = Items.DRAGON_CROSSBOW, firstMaterial = Items.DRAGON_CROSSBOW_U, secondMaterial = Items.CROSSBOW_STRING, level = 78, animation = 6677, fletchingXP = 70.0),
    // TODO: DRAGON CROSSBOW ANIMATIONS NEED TO BE FIXED

    // Unstrung Crossbows
    BRONZE_CROSSBOW_U(id = Items.BRONZE_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.WOODEN_STOCK, secondMaterial = Items.BRONZE_LIMBS, level = 9, animation = 4436, fletchingXP = 12.0),
    BLURITE_CROSSBOW_U(id = Items.BLURITE_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.OAK_STOCK, secondMaterial = Items.BLURITE_LIMBS, level = 24, animation = 4437, fletchingXP = 32.0),
    IRON_CROSSBOW_U(id = Items.IRON_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.WILLOW_STOCK, secondMaterial = Items.IRON_LIMBS, level = 39, animation = 4438, fletchingXP = 44.0),
    STEEL_CROSSBOW_U(id = Items.STEEL_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.TEAK_STOCK, secondMaterial = Items.STEEL_LIMBS, level = 46, animation = 4439, fletchingXP = 54.0),
    MITHRIL_CROSSBOW_U(id = Items.MITHRIL_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.MAPLE_STOCK, secondMaterial = Items.MITHRIL_LIMBS, level = 54, animation = 4440, fletchingXP = 64.0),
    ADAMANT_CROSSBOW_U(id = Items.ADAMANT_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.MAHOGANY_STOCK, secondMaterial = Items.ADAMANTITE_LIMBS, level = 61, animation = 4441, fletchingXP = 82.0),
    RUNITE_CROSSBOW_U(id = Items.RUNITE_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.YEW_STOCK, secondMaterial = Items.RUNITE_LIMBS, level = 69, animation = 4442, fletchingXP = 100.0),
    DRAGON_CROSSBOW_U(id = Items.DRAGON_CROSSBOW_U, toolRequired = Items.HAMMER, firstMaterial = Items.MAGIC_STOCK, secondMaterial = Items.DRAGON_LIMBS, level = 78, animation = 4442, fletchingXP = 135.0),
    // TODO: DRAGON CROSSBOW ANIMATIONS NEED TO BE FIXED

    // Ballistae
    INCOMPLETE_LIGHT_BALLISTA(id = Items.INCOMPLETE_LIGHT_BALLISTA, firstMaterial = Items.LIGHT_FRAME, secondMaterial = Items.BALLISTA_LIMBS, level = 47, animation = -1, fletchingXP = 15.0),
    UNSTRUNG_LIGHT_BALLISTA(id = Items.UNSTRUNG_LIGHT_BALLISTA, firstMaterial = Items.INCOMPLETE_LIGHT_BALLISTA, secondMaterial = Items.BALLISTA_SPRING, level = 47, animation = -1, fletchingXP = 15.0),
    LIGHT_BALLISTA(id = Items.LIGHT_BALLISTA, firstMaterial = Items.UNSTRUNG_LIGHT_BALLISTA, secondMaterial = Items.MONKEY_TAIL, level = 47, animation = -1, fletchingXP = 300.0),
    INCOMPLETE_HEAVY_BALLISTA(id = Items.INCOMPLETE_HEAVY_BALLISTA, firstMaterial = Items.HEAVY_FRAME, secondMaterial = Items.BALLISTA_LIMBS, level = 72, animation = -1, fletchingXP = 30.0),
    UNSTRUNG_HEAVY_BALLISTA(id = Items.UNSTRUNG_HEAVY_BALLISTA, firstMaterial = Items.INCOMPLETE_HEAVY_BALLISTA, secondMaterial = Items.BALLISTA_SPRING, level = 72, animation = -1, fletchingXP = 30.0),
    HEAVY_BALLISTA(id = Items.HEAVY_BALLISTA, firstMaterial = Items.UNSTRUNG_HEAVY_BALLISTA, secondMaterial = Items.MONKEY_TAIL, level = 72, animation = -1, fletchingXP = 600.0),

    // Mith Grapple
    MITH_GRAPPLE(id = Items.MITH_GRAPPLE, firstMaterial = Items.MITH_GRAPPLE_TIP, secondMaterial = Items.MITHRIL_BOLTS, level = 59, animation = -1, fletchingXP = 11.0),
    MITH_GRAPPLE_9419(id = Items.MITH_GRAPPLE_9419, firstMaterial = Items.MITH_GRAPPLE, secondMaterial = Items.ROPE, level = 59, animation = -1, fletchingXP = 0.0),
    ;

    companion object {
        /**
         * The map of the attached ids to its definition
         */
        val attachedDefinitions = values().associate { it.id to it}

    }
}