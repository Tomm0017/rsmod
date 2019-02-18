package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class AxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = Items.BRONZE_AXE, level = 1, animation = 879),
    IRON(item = Items.IRON_AXE, level = 1, animation = 877),
    STEEL(item = Items.STEEL_AXE, level = 6, animation = 875),
    BLACK(item = Items.BLACK_AXE, level = 11, animation = 873),
    MITHRIL(item = Items.MITHRIL_AXE, level = 21, animation = 871),
    ADAMANT(item = Items.ADAMANT_AXE, level = 31, animation = 869),
    RUNE(item = Items.RUNE_AXE, level = 41, animation = 867),
    DRAGON(item = Items.DRAGON_AXE, level = 61, animation = 2846),
    INFERNAL(item = Items.INFERNAL_AXE, level = 61, animation = 2117);

    companion object {
        val values = enumValues<AxeType>()
    }
}