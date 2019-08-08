package gg.rsmod.plugins.content.skills.firemaking

import gg.rsmod.plugins.api.cfg.Items

enum class Logs(val log: Int, val level: Int, val xp: Double) {
    NORMAL(Items.LOGS, 1, 40.0),
    ACHEY(Items.ACHEY_TREE_LOGS, 1, 40.0),
    OAK(Items.OAK_LOGS, 15, 60.0),
    WILLOW(Items.WILLOW_LOGS, 30, 90.0),
    TEAK(Items.TEAK_LOGS, 35, 105.0),
    ARCTIC_PINE(Items.ARCTIC_PINE_LOGS, 42, 125.0),
    MAPLE(Items.MAPLE_LOGS, 45, 135.0),
    MAHOGANY(Items.MAHOGANY_LOGS, 50, 157.5),
    YEW(Items.YEW_LOGS, 60, 202.5),
    MAGIC(Items.MAGIC_LOGS, 75, 303.8),
    REDWOOD(Items.REDWOOD_LOGS, 90, 350.0)
}