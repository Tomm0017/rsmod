package gg.rsmod.plugins.content.skills.crafting.data

import gg.rsmod.plugins.api.cfg.Items

    enum class LeatherItem(val id: Int, val level: Int, val leatherCount: Int, val amount: Int = 1, val ticks: Int = 4, val craftXp: Double) {
    LEATHER_GLOVES(id = Items.LEATHER_GLOVES, level = 1, leatherCount = 1, craftXp = 13.8),
    LEATHER_BOOTS(id = Items.LEATHER_BOOTS, level = 7, leatherCount = 1, craftXp = 16.25),
    LEATHER_COWL(id = Items.LEATHER_COWL, level = 9, leatherCount = 1, craftXp = 18.5),
    LEATHER_VAMBRACES(id = Items.LEATHER_VAMBRACES, leatherCount = 1, level = 11, craftXp = 22.0),
    LEATHER_BODY(id = Items.LEATHER_BODY, level = 14, leatherCount = 1, craftXp = 25.0),
    LEATHER_CHAPS(id = Items.LEATHER_CHAPS, level = 18, leatherCount = 1, craftXp = 27.0),
    COIF(id = Items.COIF, level = 38, leatherCount = 1, craftXp = 37.0),

    HARDLEATHER_BODY(id = Items.HARDLEATHER_BODY, leatherCount = 1, level = 28, craftXp = 35.0),

    GREEN_DHIDE_VAMB(id = Items.GREEN_DHIDE_VAMB, leatherCount = 1, level = 57, craftXp = 62.0),
    GREEN_DHIDE_CHAPS(id = Items.GREEN_DHIDE_CHAPS, leatherCount = 2, level = 60, craftXp = 124.0),
    GREEN_DHIDE_BODY(id = Items.GREEN_DHIDE_BODY, leatherCount = 3, level = 63, craftXp = 186.0),

    BLUE_DHIDE_VAMB(id = Items.BLUE_DHIDE_VAMB, leatherCount = 1, level = 66, craftXp = 70.0),
    BLUE_DHIDE_CHAPS(id = Items.BLUE_DHIDE_CHAPS, leatherCount = 2, level = 68, craftXp = 140.0),
    BLUE_DHIDE_BODY(id = Items.BLUE_DHIDE_BODY, leatherCount = 3, level = 71, craftXp = 210.0),

    RED_DHIDE_VAMB(id = Items.RED_DHIDE_VAMB, leatherCount = 1, level = 73, craftXp = 78.0),
    RED_DHIDE_CHAPS(id = Items.RED_DHIDE_CHAPS, leatherCount = 2, level = 75, craftXp = 156.0),
    RED_DHIDE_BODY(id = Items.RED_DHIDE_BODY, leatherCount = 3, level = 77, craftXp = 234.0),

    BLACK_DHIDE_VAMB(id = Items.BLACK_DHIDE_VAMB, leatherCount = 1, level = 79, craftXp = 86.0),
    BLACK_DHIDE_CHAPS(id = Items.BLACK_DHIDE_CHAPS, leatherCount = 2, level = 82, craftXp = 172.0),
    BLACK_DHIDE_BODY(id = Items.BLACK_DHIDE_BODY, leatherCount = 3, level = 84, craftXp = 258.0),

    SNAKESKIN_BOOTS(id = Items.SNAKESKIN_BOOTS, leatherCount = 6, level = 45, craftXp = 30.0),
    SNAKESKIN_VAMBRACES(id = Items.SNAKESKIN_VAMBRACES, leatherCount = 8, level = 47, craftXp = 35.0),
    SNAKESKIN_BANDANA(id = Items.SNAKESKIN_BANDANA, leatherCount = 5, level = 48, craftXp = 45.0),
    SNAKESKIN_CHAPS(id = Items.SNAKESKIN_CHAPS, leatherCount = 12, level = 51, craftXp = 50.0),
    SNAKESKIN_BODY(id = Items.SNAKESKIN_BODY, leatherCount = 15, level = 53, craftXp = 55.0),

    YAKHIDE_ARMOUR_LEGS(id = Items.YAKHIDE_ARMOUR_10824, leatherCount = 1, level = 43, craftXp = 32.0),
    YAKHIDE_ARMOUR_TOP(id = Items.YAKHIDE_ARMOUR, leatherCount = 2, level = 46, craftXp = 32.0);
}