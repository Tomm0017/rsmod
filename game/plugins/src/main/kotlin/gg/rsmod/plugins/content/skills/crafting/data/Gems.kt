package gg.rsmod.plugins.content.skills.crafting.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Triston Plummer ("Dread')
 * @editor Pitch Blac23
 */
enum class Gems(val id: Int,val prefix: String, val uncutGem: Int, val level: Int, val craftXp: Double = 0.0, val animation: Int) {
    OPAL(id = Items.OPAL, prefix = "opal", uncutGem = Items.UNCUT_OPAL, level = 1, craftXp = 15.0, animation = 890),
    JADE(id = Items.JADE, prefix = "jades", uncutGem = Items.UNCUT_JADE, level = 13, craftXp = 20.0, animation = 891),
    REDTOPAZ(id = Items.RED_TOPAZ, prefix = "red topaz", uncutGem = Items.UNCUT_RED_TOPAZ, level = 16, craftXp = 25.0, animation = 888),
    SAPPHIRE(id = Items.SAPPHIRE, prefix = "sapphires", uncutGem = Items.UNCUT_SAPPHIRE, level = 20, craftXp = 50.0, animation = 889),
    EMERALD(id = Items.EMERALD, prefix = "emeralds", uncutGem = Items.UNCUT_EMERALD, level = 27, craftXp = 65.5, animation = 887),
    RUBY(id = Items.RUBY, prefix = "rubies", uncutGem = Items.UNCUT_RUBY, level = 34, craftXp = 85.0, animation = 886),
    DIAMOND(id = Items.DIAMOND, prefix = "diamonds", uncutGem = Items.UNCUT_DIAMOND, level = 43, craftXp = 107.5, animation = 885),
    DRAGONSTONE(id = Items.DRAGONSTONE, prefix = "dragonstones", uncutGem = Items.UNCUT_DRAGONSTONE, level = 55, craftXp = 137.5, animation = 885),
    ONYX(id = Items.ONYX, prefix = "onyx", uncutGem = Items.UNCUT_ONYX, level = 67, craftXp = 167.5, animation = 885),
    ZENYTE(id = Items.ZENYTE, prefix = "zenytes", uncutGem = Items.UNCUT_ZENYTE, level = 89, craftXp = 200.0, animation = 885);

    companion object {
        /**
         * The cached array of enum definitions
         */
        val values = enumValues<Gems>()
    }
}