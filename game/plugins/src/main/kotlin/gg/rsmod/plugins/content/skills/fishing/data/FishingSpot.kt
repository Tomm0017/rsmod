package gg.rsmod.plugins.content.skills.fishing.data

import gg.rsmod.plugins.api.cfg.Npcs

/**
 * @author Vashmeed
 */
enum class FishingSpot(val npcIds: IntArray, val fishingOption: Array<FishingOption>) {
    SMALL_NET_BAIT_EEL(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1497, Npcs.FISHING_SPOT_1498, Npcs.FISHING_SPOT_1514
            ),
            fishingOption = arrayOf(FishingOption.SMALL_NET, FishingOption.BAIT_EEL)
    ),
    LURE_BAIT(
            npcIds = intArrayOf(
                    Npcs.ROD_FISHING_SPOT, Npcs.ROD_FISHING_SPOT_1507, Npcs.ROD_FISHING_SPOT_1508, Npcs.ROD_FISHING_SPOT_1509, Npcs.ROD_FISHING_SPOT_1512,
                    Npcs.ROD_FISHING_SPOT_1513, Npcs.ROD_FISHING_SPOT_1515, Npcs.ROD_FISHING_SPOT_1516, Npcs.ROD_FISHING_SPOT_1526, Npcs.ROD_FISHING_SPOT_1527, Npcs.ROD_FISHING_SPOT_1529,
                    Npcs.ROD_FISHING_SPOT_1531, Npcs.ROD_FISHING_SPOT_3417, Npcs.ROD_FISHING_SPOT_3418, Npcs.ROD_FISHING_SPOT_7463, Npcs.ROD_FISHING_SPOT_7464, Npcs.ROD_FISHING_SPOT_7468,
                    Npcs.ROD_FISHING_SPOT_8524
            ),
            fishingOption = arrayOf(FishingOption.LURE, FishingOption.L_BAIT)
    ),
    CAGE_HARPOON(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1510, Npcs.FISHING_SPOT_1519, Npcs.FISHING_SPOT_1522, Npcs.FISHING_SPOT_1533, Npcs.FISHING_SPOT_2146, Npcs.FISHING_SPOT_3657, Npcs.FISHING_SPOT_3914,
                    Npcs.FISHING_SPOT_5820, Npcs.FISHING_SPOT_7199, Npcs.FISHING_SPOT_7460, Npcs.FISHING_SPOT_7946, Npcs.FISHING_SPOT_7470, Npcs.FISHING_SPOT_7465),
            fishingOption = arrayOf(FishingOption.CAGE, FishingOption.HARPOON)
    ),
    NET_HARPOON(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1511, Npcs.FISHING_SPOT_5821, Npcs.FISHING_SPOT_4316, Npcs.FISHING_SPOT_8525, Npcs.FISHING_SPOT_8526, Npcs.FISHING_SPOT_8527),
            fishingOption = arrayOf(FishingOption.NET, FishingOption.N_HARPOON)
    ),
    SMALL_NET_BAIT(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1517, Npcs.FISHING_SPOT_1518, Npcs.FISHING_SPOT_1521, Npcs.FISHING_SPOT_1523, Npcs.FISHING_SPOT_1524, Npcs.FISHING_SPOT_1525,
                    Npcs.FISHING_SPOT_1528, Npcs.FISHING_SPOT_1544, Npcs.FISHING_SPOT_3913, Npcs.FISHING_SPOT_7155, Npcs.FISHING_SPOT_7459, Npcs.FISHING_SPOT_7462, Npcs.FISHING_SPOT_7467,
                    Npcs.FISHING_SPOT_7947, Npcs.FISHING_SPOT_7469, Npcs.FISHING_SPOT_1532
            ),
            fishingOption = arrayOf(FishingOption.SMALL_NET, FishingOption.BAIT)
    ),
    BIG_NET_HARPOON(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1520, Npcs.FISHING_SPOT_1534, Npcs.FISHING_SPOT_3419, Npcs.FISHING_SPOT_3915, Npcs.FISHING_SPOT_4476, Npcs.FISHING_SPOT_4477, Npcs.FISHING_SPOT_5233,
                    Npcs.FISHING_SPOT_5234, Npcs.FISHING_SPOT_7200, Npcs.FISHING_SPOT_7461, Npcs.FISHING_SPOT_7466
            ),
            fishingOption = arrayOf(FishingOption.BIG_NET, FishingOption.BARB_HARPOON)
    ),
    NET_BAIT(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1530, Npcs.FISHING_SPOT_1499, Npcs.FISHING_SPOT_1500
            ),
            fishingOption = arrayOf(FishingOption.NET, FishingOption.BAIT)
    ),
    CAGE(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1535, Npcs.FISHING_SPOT_1536
            ),
            fishingOption = arrayOf(FishingOption.CAGE)
    ),
    USE_ROD(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_1542, Npcs.FISHING_SPOT_7323
            ),
            fishingOption = arrayOf(FishingOption.USE_ROD)
    ),
    BAIT(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_2653, Npcs.FISHING_SPOT_2654, Npcs.FISHING_SPOT_2655, Npcs.FISHING_SPOT_4079, Npcs.FISHING_SPOT_4080, Npcs.FISHING_SPOT_4081, Npcs.FISHING_SPOT_4082,
                    Npcs.FISHING_SPOT_4928, Npcs.FISHING_SPOT_6488, Npcs.FISHING_SPOT_6784, Npcs.ROD_FISHING_SPOT_6825, Npcs.ROD_FISHING_SPOT_7676
            ),
            fishingOption = arrayOf(FishingOption.BAIT)
    ),
    NET(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_3317, Npcs.FISHING_SPOT_4710, Npcs.FISHING_SPOT_4711
            ),
            fishingOption = arrayOf(FishingOption.H_NET)
    ),
    FISH(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_4712, Npcs.FISHING_SPOT_4713, Npcs.FISHING_SPOT_4714
            ),
            fishingOption = arrayOf(FishingOption.FISH)
    ),
    SMALL_NET(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_6731, Npcs.FISHING_SPOT_7730, Npcs.FISHING_SPOT_7731, Npcs.FISHING_SPOT_7732, Npcs.FISHING_SPOT_7733
            ),
            fishingOption = arrayOf(FishingOption.SMALL_NET)
    ),
    CATCH(
            npcIds = intArrayOf(
                    Npcs.FISHING_SPOT_8523
            ),
            fishingOption = arrayOf(FishingOption.CATCH)
    );

    companion object {
        val values = enumValues<FishingSpot>()
    }
}