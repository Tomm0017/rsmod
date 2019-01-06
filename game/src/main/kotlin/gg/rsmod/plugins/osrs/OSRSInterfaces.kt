package gg.rsmod.plugins.osrs

import gg.rsmod.game.model.interf.DisplayMode

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class InterfacePane(val interfaceId: Int, val fixedChildId: Int, val resizeChildId: Int, val resizeListChildId: Int,
                         val clickThrough: Boolean = true) {
    CHAT_BOX(interfaceId = 162, fixedChildId = 24, resizeChildId = 29, resizeListChildId = 31),

    USERNAME(interfaceId = 163, fixedChildId = 19, resizeChildId = 9, resizeListChildId = 9),

    PVP_OVERLAY(interfaceId = -1, fixedChildId = 15, resizeChildId = 4, resizeListChildId = 4),

    MINI_MAP(interfaceId = 160, fixedChildId = 11, resizeChildId = 28, resizeListChildId = 28),

    XP_COUNTER(interfaceId = 122, fixedChildId = 17, resizeChildId = 7, resizeListChildId = 8),

    SKILLS(interfaceId = 320, fixedChildId = 67, resizeChildId = 69, resizeListChildId = 67),

    QUEST(interfaceId = 399, fixedChildId = 68, resizeChildId = 70, resizeListChildId = 68),

    INVENTORY(interfaceId = 149, fixedChildId = 69, resizeChildId = 71, resizeListChildId = 69),

    EQUIPMENT(interfaceId = 387, fixedChildId = 70, resizeChildId = 72, resizeListChildId = 70),

    PRAYER(interfaceId = 541, fixedChildId = 71, resizeChildId = 73, resizeListChildId = 71),

    MAGIC(interfaceId = 218, fixedChildId = 72, resizeChildId = 74, resizeListChildId = 72),

    ACCOUNT_MANAGEMENT(interfaceId = 109, fixedChildId = 74, resizeChildId = 76, resizeListChildId = 74),

    SOCIAL(interfaceId = 429, fixedChildId = 75, resizeChildId = 77, resizeListChildId = 75), // 432 = ignore

    LOG_OUT(interfaceId = 182, fixedChildId = 76, resizeChildId = 78, resizeListChildId = 76),

    SETTINGS(interfaceId = 261, fixedChildId = 77, resizeChildId = 79, resizeListChildId = 77),

    EMOTES(interfaceId = 216, fixedChildId = 78, resizeChildId = 80, resizeListChildId = 78),

    MUSIC(interfaceId = 239, fixedChildId = 79, resizeChildId = 81, resizeListChildId = 79),

    CLAN_CHAT(interfaceId = 7, fixedChildId = 73, resizeChildId = 75, resizeListChildId = 73),

    ATTACK(interfaceId = 593, fixedChildId = 66, resizeChildId = 68, resizeListChildId = 66),

    FULL_OVERLAY(interfaceId = -1, fixedChildId = 20, resizeChildId = 13, resizeListChildId = 13,
            clickThrough = false),

    CLAN_WARS(interfaceId = -1, fixedChildId = 15, resizeChildId = 6, resizeListChildId = 4),

    IRON_WINCH2(interfaceId = -1, fixedChildId = 17, resizeChildId = 8, resizeListChildId = 8,
            clickThrough = false),

    MAIN_SCREEN(interfaceId = -1, fixedChildId = 21, resizeChildId = 13, resizeListChildId = 13,
            clickThrough = false),

    TAB_AREA(interfaceId = -1, fixedChildId = 64, resizeChildId = 66, resizeListChildId = 64,
            clickThrough = false),

    WALKABLE(interfaceId = -1, fixedChildId = 14, resizeChildId = 3, resizeListChildId = 3),

    WORLD_MAP(interfaceId = -1, fixedChildId = 22, resizeChildId = 14, resizeListChildId = 14),

    WORLD_MAP_FULL(interfaceId = -1, fixedChildId = 27, resizeChildId = 27, resizeListChildId = 27,
            clickThrough = false),
}

enum class GameframeTab(val id: Int) {
    ATTACK(id = 0),
    SKILLS(id = 1),
    QUEST(id = 2),
    INVENTORY(id = 3),
    EQUIPMENT(id = 4),
    PRAYER(id = 5),
    MAGIC(id = 6),
    CLAN_CHAT(id = 7),
    FRIENDS(id = 8),
    IGNORES(id = 9),
    LOG_OUT(id = 10),
    SETTINGS(id = 11),
    EMOTES(id = 12),
    MUSIC(id = 13)
}

fun getDisplayInterfaceId(displayMode: DisplayMode) = when (displayMode) {
    DisplayMode.FIXED -> 548
    DisplayMode.RESIZABLE_NORMAL -> 161
    DisplayMode.RESIZABLE_LIST -> 164
    DisplayMode.FULLSCREEN -> 165
    else -> throw RuntimeException("Unhandled display mode.")
}

fun getChildId(pane: InterfacePane, displayMode: DisplayMode): Int = when (displayMode) {
    DisplayMode.FIXED -> pane.fixedChildId
    DisplayMode.RESIZABLE_NORMAL -> pane.resizeChildId
    DisplayMode.RESIZABLE_LIST -> pane.resizeListChildId
    else -> throw RuntimeException("Unhandled display mode.")
}