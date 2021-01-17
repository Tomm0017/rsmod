package gg.rsmod.plugins.content.mechanics.water

import gg.rsmod.plugins.api.cfg.Objs

enum class WaterSources(val message: String, vararg val waterObjIds: Int){
    FOUNTAINS("You fill the #ITEM from the fountain.", *Waters.fountains.toIntArray()),
    SINKS("You fill the #ITEM from the sink.", *Waters.sinks.toIntArray()),
    BARRELS("You fill the #ITEM.", *Waters.barrels.toIntArray()),
    PUMPS("You fill the #ITEM.", *Waters.pumps.toIntArray()),
    TAPS("You fill the #ITEM.", *Waters.taps.toIntArray()),
    WELLS("You fill the #ITEM careful not to fall in.", *Waters.wells.toIntArray());
}

object Waters {
    val fountains = arrayOf(Objs.FOUNTAIN, Objs.FOUNTAIN_879, Objs.FOUNTAIN_880, Objs.FOUNTAIN_2864, Objs.FOUNTAIN_3641, Objs.FOUNTAIN_5125,
            Objs.FOUNTAIN_6232, Objs.FOUNTAIN_7143, Objs.FOUNTAIN_10436, Objs.FOUNTAIN_10437, Objs.FOUNTAIN_10827, Objs.FOUNTAIN_11007, Objs.FOUNTAIN_12941,
            Objs.FOUNTAIN_22973, Objs.FOUNTAIN_24102, Objs.FOUNTAIN_27536, Objs.FOUNTAIN_39162, Objs.SMALL_FOUNTAIN_6749, Objs.LARGE_FOUNTAIN_6750)
    val sinks = arrayOf(Objs.SINK_873, Objs.SINK_874, Objs.SINK_1763, Objs.SINK_3014, Objs.SINK_4063, Objs.SINK_6151, Objs.SINK_7422,
            Objs.SINK_8699, Objs.SINK_9143, Objs.SINK_9684, Objs.SINK_10175, Objs.SINK_12279, Objs.SINK_12609, Objs.SINK_12974,
            Objs.SINK_13563, Objs.SINK_13564, Objs.SINK_14868, Objs.SINK_15678, Objs.SINK_16704, Objs.SINK_16705, Objs.SINK_20358, Objs.SINK_22715,
            Objs.SINK_25729, Objs.SINK_25929, Objs.SINK_27707, Objs.SINK_27708, Objs.SINK_28538, Objs.SINK_34943, Objs.SINK_39393, Objs.SINK_39459,
            Objs.SINK_39489, Objs.SINK_40023, Objs.TOY_SINK)
    val barrels = arrayOf(Objs.WATER_BARREL, Objs.WATER_BARREL_5599, Objs.WATER_BARREL_8702, Objs.WATER_BARREL_8703)
    val pumps = arrayOf(Objs.WATER_PUMP, Objs.WATER_PUMP_15937, Objs.WATER_PUMP_15938, Objs.WATER_PUMP_35981, Objs.WATER_PUMP_36078)
    val taps = arrayOf(Objs.WATER_TAP, Objs.TAP, Objs.TAP_4285, Objs.TAP_4482, Objs.TAP_8737, Objs.TAP_20794)
    val wells = arrayOf(Objs.WELL, Objs.WELL_884, Objs.WELL_3264, Objs.WELL_3305, Objs.WELL_3359, Objs.WELL_3485, Objs.WELL_3646,
            Objs.WELL_4004, Objs.WELL_4005, Objs.WELL_6097, Objs.WELL_6249, Objs.WELL_6549, Objs.WELL_8747, Objs.WELL_8927, Objs.WELL_12201,
            Objs.WELL_12897, Objs.WELL_24150, Objs.WELL_29100, Objs.WELL_30930, Objs.WELL_35881, Objs.WELL_39720)
}