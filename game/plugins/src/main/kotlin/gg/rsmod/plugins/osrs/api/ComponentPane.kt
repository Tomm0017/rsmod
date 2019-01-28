package gg.rsmod.plugins.osrs.api

import gg.rsmod.game.model.interf.DisplayMode

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ComponentPane(val component: Int, val fixedChildId: Int, val resizeChildId: Int, val resizeListChildId: Int,
                         val fullscreenChildId: Int = -1, val clickThrough: Boolean = true) {

    CHAT_BOX(component = 162, fixedChildId = 24, resizeChildId = 29, resizeListChildId = 31),

    USERNAME(component = 163, fixedChildId = 19, resizeChildId = 9, resizeListChildId = 9),

    PVP_OVERLAY(component = -1, fixedChildId = 15, resizeChildId = 4, resizeListChildId = 4),

    MINI_MAP(component = 160, fixedChildId = 11, resizeChildId = 28, resizeListChildId = 28),

    XP_COUNTER(component = 122, fixedChildId = 17, resizeChildId = 7, resizeListChildId = 7),

    SKILLS(component = 320, fixedChildId = 67, resizeChildId = 69, resizeListChildId = 69),

    QUEST(component = 399, fixedChildId = 68, resizeChildId = 70, resizeListChildId = 70),

    INVENTORY(component = 149, fixedChildId = 69, resizeChildId = 71, resizeListChildId = 71),

    EQUIPMENT(component = 387, fixedChildId = 70, resizeChildId = 72, resizeListChildId = 72),

    PRAYER(component = 541, fixedChildId = 71, resizeChildId = 73, resizeListChildId = 73),

    MAGIC(component = 218, fixedChildId = 72, resizeChildId = 74, resizeListChildId = 74),

    ACCOUNT_MANAGEMENT(component = 109, fixedChildId = 74, resizeChildId = 76, resizeListChildId = 76),

    SOCIAL(component = 429, fixedChildId = 75, resizeChildId = 77, resizeListChildId = 77), // 432 = ignore

    LOG_OUT(component = 182, fixedChildId = 76, resizeChildId = 78, resizeListChildId = 78),

    SETTINGS(component = 261, fixedChildId = 77, resizeChildId = 79, resizeListChildId = 79),

    EMOTES(component = 216, fixedChildId = 78, resizeChildId = 80, resizeListChildId = 80),

    MUSIC(component = 239, fixedChildId = 79, resizeChildId = 81, resizeListChildId = 81),

    CLAN_CHAT(component = 7, fixedChildId = 73, resizeChildId = 75, resizeListChildId = 75),

    ATTACK(component = 593, fixedChildId = 66, resizeChildId = 68, resizeListChildId = 68),

    MAIN_SCREEN(component = -1, fixedChildId = 21, resizeChildId = 13, resizeListChildId = 13,
            clickThrough = false),

    TAB_AREA(component = -1, fixedChildId = 64, resizeChildId = 66, resizeListChildId = 66,
            clickThrough = false),

    WALKABLE(component = -1, fixedChildId = 14, resizeChildId = 3, resizeListChildId = 3),

    WORLD_MAP(component = -1, fixedChildId = 22, resizeChildId = 14, resizeListChildId = 14,
            fullscreenChildId = 28),

    WORLD_MAP_FULL(component = -1, fixedChildId = 27, resizeChildId = 27, resizeListChildId = 27,
            fullscreenChildId = 27, clickThrough = false);

    fun isSwitchable(): Boolean = when (this) {
        CHAT_BOX, MAIN_SCREEN, WALKABLE, TAB_AREA,
            ATTACK, SKILLS, QUEST, INVENTORY, EQUIPMENT,
            PRAYER, MAGIC, CLAN_CHAT, ACCOUNT_MANAGEMENT,
            SOCIAL, LOG_OUT, SETTINGS, EMOTES, MUSIC, PVP_OVERLAY,
            USERNAME, MINI_MAP, XP_COUNTER, WORLD_MAP -> true
        else -> false
    }
}

fun getDisplayComponentId(displayMode: DisplayMode) = when (displayMode) {
    DisplayMode.FIXED -> 548
    DisplayMode.RESIZABLE_NORMAL -> 161
    DisplayMode.RESIZABLE_LIST -> 164
    DisplayMode.FULLSCREEN -> 165
    else -> throw RuntimeException("Unhandled display mode.")
}

fun getChildId(pane: ComponentPane, displayMode: DisplayMode): Int = when (displayMode) {
    DisplayMode.FIXED -> pane.fixedChildId
    DisplayMode.RESIZABLE_NORMAL -> pane.resizeChildId
    DisplayMode.RESIZABLE_LIST -> pane.resizeListChildId
    DisplayMode.FULLSCREEN -> pane.fullscreenChildId
    else -> throw RuntimeException("Unhandled display mode.")
}