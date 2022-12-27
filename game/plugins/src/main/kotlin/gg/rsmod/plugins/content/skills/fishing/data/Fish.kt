package gg.rsmod.plugins.content.skills.fishing.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Vashmeed
 */
enum class Fish(val item: Int, val levelReq: Int, val experience: Double) {
    SHRIMP(
            item = Items.RAW_SHRIMPS,
            levelReq = 1,
            experience = 10.0
    ),
    SARDINE(
            item = Items.RAW_SARDINE,
            levelReq = 5,
            experience = 20.0
    ),
    KARAMBWANJI(
            item = Items.RAW_KARAMBWANJI,
            levelReq = 5,
            experience = 5.0
    ),
    HERRING(
            item = Items.RAW_HERRING,
            levelReq = 10,
            experience = 30.0
    ),
    ANCHOVIE(
            item = Items.RAW_ANCHOVIES,
            levelReq = 15,
            experience = 40.0
    ),
    MACKEREL(
            item = Items.RAW_MACKEREL,
            levelReq = 16,
            experience = 20.0
    ),
    TROUT(
            item = Items.RAW_TROUT,
            levelReq = 20,
            experience = 50.0
    ),
    COD(
            item = Items.RAW_COD,
            levelReq = 23,
            experience = 45.0
    ),
    PIKE(
            item = Items.RAW_PIKE,
            levelReq = 25,
            experience = 60.0
    ),
    SLIMY_EEL(
            item = Items.RAW_SLIMY_EEL,
            levelReq = 28,
            experience = 65.0
    ),
    SALMON(
            item = Items.RAW_SALMON,
            levelReq = 30,
            experience = 70.0
    ),
    FROG_SPAWN(
            item = Items.FROG_SPAWN,
            levelReq = 33,
            experience = 75.0
    ),
    TUNA(
            item = Items.RAW_TUNA,
            levelReq = 35,
            experience = 80.0
    ),
    RAINBOW_FISH(
            item = Items.RAW_RAINBOW_FISH,
            levelReq = 38,
            experience = 80.0
    ),
    CAVE_EEL(
            item = Items.RAW_CAVE_EEL,
            levelReq = 38,
            experience = 80.0
    ),
    LOBSTER(
            item = Items.RAW_LOBSTER,
            levelReq = 40,
            experience = 90.0
    ),
    BASS(
            item = Items.RAW_BASS,
            levelReq = 46,
            experience = 100.0
    ),
    SWORDFISH(
            item = Items.RAW_SWORDFISH,
            levelReq = 50,
            experience = 100.0
    ),
    LAVA_EEL(
            item = Items.RAW_LAVA_EEL,
            levelReq = 53,
            experience = 100.0
    ),
    MONKFISH(
            item = Items.RAW_MONKFISH,
            levelReq = 62,
            experience = 120.0
    ),
    KARAMBWAN(
            item = Items.RAW_KARAMBWAN,
            levelReq = 65,
            experience = 105.0
    ),
    SHARK(
            item = Items.RAW_SHARK,
            levelReq = 76,
            experience = 110.0
    ),
    SEA_TURTLE(
            item = Items.RAW_SEA_TURTLE,
            levelReq = 79,
            experience = 38.0
    ),
    MANTA_RAY(
            item = Items.RAW_MANTA_RAY,
            levelReq = 81,
            experience = 46.0
    ),
    SEAWEED(
            item = Items.SEAWEED,
            levelReq = 16,
            experience = 1.0
    ),
    CASKET(
            item = Items.CASKET,
            levelReq = 16,
            experience = 10.0
    ),
    OYSTER(
            item = Items.OYSTER,
            levelReq = 16,
            experience = 10.0
    ),
    DARK_CRAB(
            item = Items.RAW_DARK_CRAB,
            levelReq = 85,
            experience = 130.0
    );

    companion object {
        val values = enumValues<Fish>()
    }
}