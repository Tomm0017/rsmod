package gg.rsmod.plugins.content.areas.wilderness.region

private val WILDERNESS_MULTI_REGIONS = intArrayOf(12088, 12089, 12599, 12600, 12604,
        12855, 12856, 12857, 12858, 12859, 12860, 12861,
        13111, 13112, 13113, 13114, 13115, 13116, 13117, 13372, 13373)

WILDERNESS_MULTI_REGIONS.forEach { set_multi_combat_region(region = it) }
