package gg.rsmod.plugins.content.skills.fishing

import gg.rsmod.plugins.api.cfg.Items

enum class Fish(val fishItem: Int, val level: Int, val xp: Double) {
    SHRIMP(Items.RAW_SHRIMPS, 1, 10.0),
    SARDINE(Items.RAW_SARDINE, 5, 20.0),
    HERRING(Items.RAW_HERRING, 10, 30.0),
    ANCHOVIES(Items.RAW_ANCHOVIES, 15, 40.0),
    TROUT(Items.RAW_TROUT, 20, 50.0),
    PIKE(Items.RAW_PIKE, 25, 60.0),
    SALMON(Items.RAW_SALMON, 30, 70.0),
    TUNA(Items.RAW_TUNA, 35, 80.0),
    RAINBOWFISH(Items.RAW_RAINBOW_FISH, 38, 80.0),
    LOBSTER(Items.RAW_LOBSTER, 40, 90.0),
    SWORDFISH(Items.RAW_SWORDFISH, 50, 100.0),
    MONKFISH(Items.RAW_MONKFISH, 62, 120.0),
    SHARK(Items.RAW_SHARK, 76, 110.0)
}