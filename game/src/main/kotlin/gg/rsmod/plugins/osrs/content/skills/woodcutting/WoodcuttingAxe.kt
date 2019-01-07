package gg.rsmod.plugins.osrs.content.skills.woodcutting

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class WoodcuttingAxe(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = 1351, level = 1, animation = 879),
    IRON(item = 1349, level = 1, animation = 877),
    STEEL(item = 1353, level = 6, animation = 875),
    BLACK(item = 1361, level = 11, animation = 873),
    MITHRIL(item = 1355, level = 21, animation = 871),
    ADAMANT(item = 1357, level = 31, animation = 869),
    RUNE(item = 1359, level = 41, animation = 867),
    DRAGON(item = 6739, level = 61, animation = 2846),
    INFERNAL(item = 13241, level = 61, animation = 2117)
}