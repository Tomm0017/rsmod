package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class PickAxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = Items.BRONZE_PICKAXE, level = 1, animation = 625),
    IRON(item = Items.IRON_PICKAXE, level = 1, animation = 626),
    STEEL(item = Items.STEEL_PICKAXE, level = 6, animation = 627),
    BLACK(item = Items.BLACK_PICKAXE, level = 11, animation = 625),
    MITHRIL(item = Items.MITHRIL_PICKAXE, level = 21, animation = 629),
    ADAMANT(item = Items.ADAMANT_PICKAXE, level = 31, animation = 628),
    RUNE(item = Items.RUNE_PICKAXE, level = 41, animation = 624),
    DRAGON(item = Items.DRAGON_PICKAXE, level = 61, animation = 7139),
    DRAGON_UPGRADED(item = Items.DRAGON_PICKAXE_12797, level = 61, animation = 642),
    INFERNAL(item = Items.INFERNAL_AXE, level = 61, animation = 4482);

    companion object {
        val values = enumValues<PickAxeType>()
    }
}