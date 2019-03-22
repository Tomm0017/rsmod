package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class PickAxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = Items.BRONZE_PICKAXE, level = 1, animation = 879),
    IRON(item = Items.IRON_PICKAXE, level = 1, animation = 877),
    STEEL(item = Items.STEEL_PICKAXE, level = 6, animation = 875),
    BLACK(item = Items.BLACK_PICKAXE, level = 11, animation = 873),
    MITHRIL(item = Items.MITHRIL_PICKAXE, level = 21, animation = 871),
    ADAMANT(item = Items.ADAMANT_PICKAXE, level = 31, animation = 869),
    RUNE(item = Items.RUNE_PICKAXE, level = 41, animation = 867),
    DRAGON(item = Items.DRAGON_PICKAXE, level = 61, animation = 2846),
    INFERNAL(item = Items.INFERNAL_AXE, level = 61, animation = 2117);

    companion object {
        val values = enumValues<PickAxeType>()
    }
}