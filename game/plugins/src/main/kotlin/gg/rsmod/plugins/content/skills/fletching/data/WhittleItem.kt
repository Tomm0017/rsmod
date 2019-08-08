package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

enum class WhittleItem(val id: Int, val level: Int, val logCount: Int = 1, val amount: Int = 1, val ticks: Int = 3, val fletchingXP: Double) {
    // Logs
    ARROW_SHAFT_15(id = Items.ARROW_SHAFT, level = 1, amount = 15, fletchingXP = 0.5),
    JAVELIN_SHAFT(id = Items.JAVELIN_SHAFT, level = 3, amount = 15, fletchingXP = 0.5),
    SHORTBOW_U(id = Items.SHORTBOW_U, level = 5, fletchingXP = 5.0),
    LONGBOW_U(id = Items.LONGBOW_U, level = 10, fletchingXP = 10.0),
    WOODEN_STOCK(id = Items.WOODEN_STOCK, level = 9, fletchingXP = 6.0),

    // Oak Logs
    ARROW_SHAFT_30(id = Items.ARROW_SHAFT, level = 15, amount = 30, fletchingXP = 0.5),
    OAK_SHORTBOW_U(id = Items.OAK_SHORTBOW_U, level = 20, fletchingXP = 16.5),
    OAK_LONGBOW_U(id = Items.OAK_LONGBOW_U, level = 25, fletchingXP = 25.0),
    OAK_STOCK(id = Items.OAK_STOCK, level = 24, fletchingXP = 16.0),
    OAK_SHIELD(id = Items.OAK_SHIELD, level = 27, ticks = 7, logCount = 2, fletchingXP = 50.0),

    // Achey Tree Logs
    OGRE_ARROW_SHAFT(id = Items.OGRE_ARROW_SHAFT, level = 5, fletchingXP = 1.6),
    UNSTRUNG_COMP_BOW(id = Items.UNSTRUNG_COMP_BOW, level = 30, fletchingXP = 45.0),

    // Willow Logs
    ARROW_SHAFT_45(id = Items.ARROW_SHAFT, level = 30, amount = 45, fletchingXP = 0.5),
    WILLOW_SHORTBOW_U(id = Items.WILLOW_SHORTBOW_U, level = 35, fletchingXP = 33.3),
    WILLOW_LONGBOW_U(id = Items.WILLOW_LONGBOW_U, level = 40, fletchingXP = 41.5),
    WILLOW_STOCK(id = Items.WILLOW_STOCK, level = 39, fletchingXP = 22.0),
    WILLOW_SHIELD(id = Items.WILLOW_SHIELD, level = 42, ticks = 7, logCount = 2, fletchingXP = 83.0),

    // Teak Logs
    TEAK_STOCK(id = Items.TEAK_STOCK, level = 46, fletchingXP = 27.0),

    // Maple Logs
    ARROW_SHAFT_60(id = Items.ARROW_SHAFT, level = 45, amount = 60, fletchingXP = 0.5),
    MAPLE_SHORTBOW_U(id = Items.MAPLE_SHORTBOW_U, level = 50, fletchingXP = 50.0),
    MAPLE_LONGBOW_U(id = Items.MAPLE_LONGBOW_U, level = 55, fletchingXP = 58.3),
    MAPLE_STOCK(id = Items.MAPLE_STOCK, level = 54, fletchingXP = 32.0),
    MAPLE_SHIELD(id = Items.OAK_SHIELD, level = 57, ticks = 7, logCount = 2, fletchingXP = 116.5),

    // Mahogany Logs
    MAHOGANY_STOCK(id = Items.MAHOGANY_STOCK, level = 61, fletchingXP = 41.0),

    // Yew Logs
    ARROW_SHAFT_75(id = Items.ARROW_SHAFT, level = 60, amount = 75, fletchingXP = 0.5),
    YEW_SHORTBOW_U(id = Items.YEW_SHORTBOW_U, level = 65, fletchingXP = 67.5),
    YEW_LONGBOW_U(id = Items.YEW_LONGBOW_U, level = 70, fletchingXP = 75.0),
    YEW_STOCK(id = Items.YEW_STOCK, level = 69, fletchingXP = 50.0),
    YEW_SHIELD(id = Items.YEW_SHIELD, level = 72, ticks = 7, logCount = 2, fletchingXP = 150.0),

    // Magic Logs
    ARROW_SHAFT_90(id = Items.ARROW_SHAFT, level = 75, amount = 90, fletchingXP = 0.5),
    MAGIC_SHORTBOW_U(id = Items.MAGIC_SHORTBOW_U, level = 80, fletchingXP = 83.3),
    MAGIC_LONGBOW_U(id = Items.MAGIC_LONGBOW_U, level = 85, fletchingXP = 91.5),
    MAGIC_STOCK(id = Items.MAGIC_STOCK, level = 78, fletchingXP = 70.0),
    MAGIC_SHIELD(id = Items.MAGIC_SHIELD, level = 87, ticks = 7, logCount = 2, fletchingXP = 183.0),

    // Redwood Logs
    ARROW_SHAFT_105(id = Items.ARROW_SHAFT, level = 90, amount = 105, fletchingXP = 0.5),
    REDWOOD_SHIELD(id = Items.REDWOOD_SHIELD, level = 92, ticks = 7, logCount = 2, fletchingXP = 216.0);
}