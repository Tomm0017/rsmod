package gg.rsmod.plugins.content.skills.crafting.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author momof513
 * @editor pitch blac23
 */
enum class Leathers(val id: Int, val leatherOptions: Array<LeatherItem>) {
    LEATHER(id=Items.LEATHER, leatherOptions = arrayOf(LeatherItem.LEATHER_GLOVES, LeatherItem.LEATHER_BOOTS, LeatherItem.LEATHER_COWL, LeatherItem.LEATHER_VAMBRACES, LeatherItem.LEATHER_BODY, LeatherItem.LEATHER_CHAPS, LeatherItem.COIF)),
    HARD_LEATHER(id=Items.HARD_LEATHER, leatherOptions = arrayOf(LeatherItem.HARDLEATHER_BODY)),
    GREEN_DRAGON_LEATHER(id=Items.GREEN_DRAGON_LEATHER, leatherOptions = arrayOf(LeatherItem.GREEN_DHIDE_VAMB, LeatherItem.GREEN_DHIDE_CHAPS, LeatherItem.GREEN_DHIDE_BODY)),
    BLUE_DRAGON_LEATHER(id=Items.BLUE_DRAGON_LEATHER, leatherOptions = arrayOf(LeatherItem.BLUE_DHIDE_VAMB, LeatherItem.BLUE_DHIDE_CHAPS, LeatherItem.BLUE_DHIDE_BODY)),
    RED_DRAGON_LEATHER(id=Items.RED_DRAGON_LEATHER, leatherOptions = arrayOf(LeatherItem.RED_DHIDE_VAMB, LeatherItem.RED_DHIDE_CHAPS, LeatherItem.RED_DHIDE_BODY)),
    BLACK_DRAGON_LEATHER(id=Items.BLACK_DRAGON_LEATHER, leatherOptions = arrayOf(LeatherItem.BLACK_DHIDE_VAMB, LeatherItem.BLACK_DHIDE_CHAPS, LeatherItem.BLACK_DHIDE_BODY)),
    YAKHIDE(id=Items.YAKHIDE, leatherOptions = arrayOf(LeatherItem.YAKHIDE_ARMOUR_LEGS, LeatherItem.YAKHIDE_ARMOUR_TOP)),
    SNAKESKIN(id=Items.SNAKESKIN, leatherOptions = arrayOf(LeatherItem.SNAKESKIN_BOOTS, LeatherItem.SNAKESKIN_VAMBRACES, LeatherItem.SNAKESKIN_BANDANA, LeatherItem.SNAKESKIN_CHAPS, LeatherItem.SNAKESKIN_BODY));

    companion object {
        /**
         * The map of log ids to a map of whittleItem ids to their definitions
         */
        val leatherDefinitions = values().associate { leathers ->
             leathers.id to leathers.leatherOptions.associate { leatherOptions ->
                leatherOptions.id to leatherOptions
            }
        }
    }
}