package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.interf.DisplayMode

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 4) {
    player.toggleVarbit(OSRSGameframe.CHATBOX_SCROLLBAR_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 6) {
    player.toggleVarbit(OSRSGameframe.DISABLE_SIDEPANELS_OPAQUE_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 8) {
    player.toggleVarbit(OSRSGameframe.DISABLE_XP_TILL_LEVEL_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 10) {
    player.toggleVarbit(OSRSGameframe.DISABLE_PRAYER_TOOLTIP_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 12) {
    player.toggleVarbit(OSRSGameframe.DISABLE_SPECIAL_ATTACK_TOOLTIP_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 16) {
    val p = player
    p.toggleVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT)

    /**
     * Close or open the XP drop interface.
     */
    if (p.getVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT) == 0) {
        p.openInterface(interfaceId = 160, dest = InterfaceDestination.MINI_MAP)
    } else {
        p.closeInterface(interfaceId = 160)
    }
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 18) {
    player.toggleVarbit(OSRSGameframe.CHATBOX_TRANSPARENT_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 20) {
    player.toggleVarbit(OSRSGameframe.CHATBOX_SOLID_VARBIT)
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 21) {
    val p = player

    p.toggleVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT)

    if (p.isClientResizable()) {
        val mode = if (player.getVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT) == 0) DisplayMode.RESIZABLE_NORMAL else DisplayMode.RESIZABLE_LIST
        p.toggleDisplayInterface(mode)
    }
}

on_button(interfaceId = OptionsTab.ADVANCED_COMPONENT_ID, component = 23) {
    player.toggleVarbit(OSRSGameframe.CLOSE_TABS_WITH_HOTKEY_VARBIT)
}