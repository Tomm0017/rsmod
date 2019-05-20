package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.game.model.Area
import gg.rsmod.plugins.content.magic.TeleportType

enum class TeleportSpell(val spellName: String, val type: TeleportType, val endArea: Area,
                         val xp: Double, val paramItem: Int? = null) {
    /**
     * Standard.
     */
    VARROCK("Varrock Teleport", TeleportType.MODERN, Area(3210, 3423, 3216, 3425), 35.0),
    LUMBRIDGE("Lumbridge Teleport", TeleportType.MODERN, Area(3221, 3218, 3222, 3219), 41.0),
    FALADOR("Falador Teleport", TeleportType.MODERN, Area(2961, 3376, 2969, 3385), 47.0),
    CAMELOT("Camelot Teleport", TeleportType.MODERN, Area(2756, 3476, 2758, 3480), 55.5),
    ARDOUGNE("Ardougne Teleport", TeleportType.MODERN, Area(2659, 3300, 2665, 3310), 61.0),
    WATCHTOWER("Watchtower Teleport", TeleportType.MODERN, Area(2551, 3113, 2553, 3116), 68.0),
    TROLLHEIM("Trollheim Teleport", TeleportType.MODERN, Area(2888, 3675, 2890, 3678), 68.0),
    APE_ATOLL("Ape Atoll Teleport", TeleportType.MODERN, Area(2760, 2781, 2763, 2784), 74.0),
    KOUREND_CASTLE("Kourend Castle Teleport", TeleportType.MODERN, Area(1633, 3665, 1639, 3670), 82.0),

    /**
     * Ancients.
     */
    PADDEWWA("Paddewwa Teleport", TeleportType.ANCIENT, Area(3095, 9880, 3099, 9884), 64.0),
    SENNTISTEN("Senntisten Teleport", TeleportType.ANCIENT, Area(3346, 3343, 3350, 3346), 70.0),
    KHARYRLL("Kharyrll Teleport", TeleportType.ANCIENT, Area(3491, 3476, 3494, 3478), 76.0),
    LASSAR("Lassar Teleport", TeleportType.ANCIENT, Area(3003, 3473, 3008, 3476), 82.0),
    DAREEYAK("Dareeyak Teleport", TeleportType.ANCIENT, Area(2965, 3693, 2969, 3697), 88.0),
    CARRALLANGAR("Carrallangar Teleport", TeleportType.ANCIENT, Area(3146, 3668, 3149, 3671), 94.0),
    ANNAKARL("Annakarl Teleport", TeleportType.ANCIENT, Area(3293, 3885, 3297, 3888), 100.0),
    GHORROCK("Ghorrock Teleport", TeleportType.ANCIENT, Area(2966, 3872, 2972, 3878), 106.0),

    /**
     * Lunars.
     */
    MOONCLAN("Moonclan Teleport", TeleportType.LUNAR, Area(2096, 3912, 2099, 3915), 66.0),
    OURANIA("Ourania Teleport", TeleportType.LUNAR, Area(2454, 3232, 2455, 3233), 69.0),
    WATERBIRTH("Waterbirth Teleport", TeleportType.LUNAR, Area(2545, 3756, 2548, 3759), 71.0),
    BARBARIAN("Barbarian Teleport", TeleportType.LUNAR, Area(2547, 3566, 2549, 3571), 76.0),
    KHAZARD("Khazard Teleport", TeleportType.LUNAR, Area(2652, 3156, 2660, 3159), 80.0),
    CATHERBY("Catherby Teleport", TeleportType.LUNAR, Area(2802, 3432, 2806, 3435), 92.0),
    ICE_PLATEAU("Ice Plateau Teleport", TeleportType.LUNAR, Area(2979, 3940, 2984, 3946), 96.0),

    /**
     * Arceuus.
     */
    LUMBRIDGE_GRAVEYARD("Lumbridge Graveyard Teleport", TeleportType.ARCEUUS, Area(3238, 3199, 3240, 3203), 10.0),
    DRAYNOR_MANOR("Draynor Manor Teleport", TeleportType.ARCEUUS, Area(3107, 3327, 3113, 3330), 16.0),
    BATTLEFRONT("Battlefront Teleport", TeleportType.ARCEUUS, Area(1342, 3682, 1346, 3685), 19.0),
    MIND_ALTAR("Mind Altar Teleport", TeleportType.ARCEUUS, Area(2979, 3509, 2980, 3512), 22.0),
    SALVE_GRAVEYARD("Salve Graveyard Teleport", TeleportType.ARCEUUS, Area(3437, 3467, 3442, 3471), 30.0),
    FENKENSTRAIN_CASTLE("Fenkenstrain's Castle Teleport", TeleportType.ARCEUUS, Area(3546, 3527, 3549, 3529), 50.0),
    WEST_ARDOUGNE("West Ardougne Teleport", TeleportType.ARCEUUS, Area(2528, 3304, 2534, 3308), 68.0),
    HARMONY_ISLAND("Harmony Island Teleport", TeleportType.ARCEUUS, Area(3793, 2857, 3801, 2863), 74.0),
    CEMETERY("Cemetery Teleport", TeleportType.ARCEUUS, Area(2964, 3760, 2969, 3766), 82.0),
    BARROWS("Barrows Teleport", TeleportType.ARCEUUS, Area(3563, 3312, 3566, 3315), 90.0),
    APE_ATOLL_DUNGEON("Ape Atoll Teleport", TeleportType.ARCEUUS, Area(2764, 9102, 2767, 9104), 100.0, paramItem = 20427)
    ;

    companion object {
        val values = enumValues<TeleportSpell>()
    }
}