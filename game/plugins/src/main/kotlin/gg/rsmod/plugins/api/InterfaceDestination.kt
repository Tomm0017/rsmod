package gg.rsmod.plugins.api

import gg.rsmod.game.model.interf.DisplayMode

enum class InterfaceDestination(val interfaceId: Int, val fixedChildId: Int, val resizeChildId: Int, val resizeListChildId: Int,
                                val fullscreenChildId: Int = -1, val clickThrough: Boolean = true) {

    CHAT_BOX(interfaceId = 162, fixedChildId = 27, resizeChildId = 32, resizeListChildId = 34, fullscreenChildId = 1),

    XP_COUNTER(interfaceId = 122, fixedChildId = 18, resizeChildId = 8, resizeListChildId = 8, fullscreenChildId = 6),

    ATTACK(interfaceId = 593, fixedChildId = 69, resizeChildId = 71, resizeListChildId = 71, fullscreenChildId = 10),

    SKILLS(interfaceId = 320, fixedChildId = 70, resizeChildId = 72, resizeListChildId = 72, fullscreenChildId = 11),

    QUEST_ROOT(interfaceId = 629, fixedChildId = 71, resizeChildId = 73, resizeListChildId = 73, fullscreenChildId = 12),

    INVENTORY(interfaceId = 149, fixedChildId = 72, resizeChildId = 74, resizeListChildId = 74, fullscreenChildId = 13),

    EQUIPMENT(interfaceId = 387, fixedChildId = 73, resizeChildId = 75, resizeListChildId = 75, fullscreenChildId = 14),

    PRAYER(interfaceId = 541, fixedChildId = 74, resizeChildId = 76, resizeListChildId = 76, fullscreenChildId = 15),

    MAGIC(interfaceId = 218, fixedChildId = 75, resizeChildId = 77, resizeListChildId = 77, fullscreenChildId = 16),

    CLAN_CHAT(interfaceId = 7, fixedChildId = 76, resizeChildId = 78, resizeListChildId = 78, fullscreenChildId = 17),

    ACCOUNT_MANAGEMENT(interfaceId = 109, fixedChildId = 77, resizeChildId = 79, resizeListChildId = 79, fullscreenChildId = 18),

    SOCIAL(interfaceId = 429, fixedChildId = 78, resizeChildId = 80, resizeListChildId = 80, fullscreenChildId = 19),

    LOG_OUT(interfaceId = 182, fixedChildId = 79, resizeChildId = 81, resizeListChildId = 81, fullscreenChildId = 20),

    SETTINGS(interfaceId = 116, fixedChildId = 80, resizeChildId = 82, resizeListChildId = 82, fullscreenChildId = 21),

    EMOTES(interfaceId = 216, fixedChildId = 81, resizeChildId = 83, resizeListChildId = 83, fullscreenChildId = 22),

    MUSIC(interfaceId = 239, fixedChildId = 82, resizeChildId = 84, resizeListChildId = 84, fullscreenChildId = 23),

    USERNAME(interfaceId = 163, fixedChildId = 20, resizeChildId = 11, resizeListChildId = 11, fullscreenChildId = 25),

    MINI_MAP(interfaceId = 160, fixedChildId = 11, resizeChildId = 31, resizeListChildId = 31, fullscreenChildId = 26),

    PVP_OVERLAY(interfaceId = -1, fixedChildId = 15, resizeChildId = 4, resizeListChildId = 4, fullscreenChildId = 24),

    MAIN_SCREEN(interfaceId = -1, fixedChildId = 23, resizeChildId = 15, resizeListChildId = 15, fullscreenChildId = 8,
            clickThrough = false),

    TAB_AREA(interfaceId = -1, fixedChildId = 67, resizeChildId = 66, resizeListChildId = 66,
            clickThrough = false),

    WALKABLE(interfaceId = -1, fixedChildId = 14, resizeChildId = 3, resizeListChildId = 3),

    WORLD_MAP(interfaceId = -1, fixedChildId = 24, resizeChildId = 16, resizeListChildId = 16, fullscreenChildId = 30),

    WORLD_MAP_FULL(interfaceId = -1, fixedChildId = 27, resizeChildId = 27, resizeListChildId = 27, fullscreenChildId = 27, clickThrough = false);

    fun isSwitchable(): Boolean = when (this) {
        CHAT_BOX, MAIN_SCREEN, WALKABLE, TAB_AREA,
        ATTACK, SKILLS, QUEST_ROOT, INVENTORY, EQUIPMENT,
        PRAYER, MAGIC, CLAN_CHAT, ACCOUNT_MANAGEMENT,
        SOCIAL, LOG_OUT, SETTINGS, EMOTES, MUSIC, PVP_OVERLAY,
        USERNAME, MINI_MAP, XP_COUNTER, WORLD_MAP -> true
        else -> false
    }

    companion object {
        val values = enumValues<InterfaceDestination>()

        fun getModals() = values.filter { pane -> pane.interfaceId != -1 }
    }
}

fun getDisplayComponentId(displayMode: DisplayMode) = when (displayMode) {
    DisplayMode.FIXED -> 548
    DisplayMode.RESIZABLE_NORMAL -> 161
    DisplayMode.RESIZABLE_LIST -> 164
    DisplayMode.FULLSCREEN -> 165
    else -> throw RuntimeException("Unhandled display mode.")
}

fun getChildId(pane: InterfaceDestination, displayMode: DisplayMode): Int = when (displayMode) {
    DisplayMode.FIXED -> pane.fixedChildId
    DisplayMode.RESIZABLE_NORMAL -> pane.resizeChildId
    DisplayMode.RESIZABLE_LIST -> pane.resizeListChildId
    DisplayMode.FULLSCREEN -> pane.fullscreenChildId
    else -> throw RuntimeException("Unhandled display mode.")
}