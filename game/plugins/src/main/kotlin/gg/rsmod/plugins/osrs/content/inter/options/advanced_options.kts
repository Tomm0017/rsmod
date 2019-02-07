
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.OSRSGameframe
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 4) {
    it.player().toggleVarbit(OSRSGameframe.CHATBOX_SCROLLBAR_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 6) {
    it.player().toggleVarbit(OSRSGameframe.DISABLE_SIDEPANELS_OPAQUE_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 8) {
    it.player().toggleVarbit(OSRSGameframe.DISABLE_XP_TILL_LEVEL_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 10) {
    it.player().toggleVarbit(OSRSGameframe.DISABLE_PRAYER_TOOLTIP_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 12) {
    it.player().toggleVarbit(OSRSGameframe.DISABLE_SPECIAL_ATTACK_TOOLTIP_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 16) {
    val p = it.player()
    p.toggleVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT)

    /**
     * Close or open the XP drop interface.
     */
    if (p.getVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT) == 0) {
        p.openInterface(component = 160, pane = InterfaceDestination.MINI_MAP)
    } else {
        p.closeInterface(interfaceId = 160)
    }
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 18) {
    it.player().toggleVarbit(OSRSGameframe.CHATBOX_TRANSPARENT_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 20) {
    it.player().toggleVarbit(OSRSGameframe.CHATBOX_SOLID_VARBIT)
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 21) {
    val p = it.player()

    p.toggleVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT)

    if (p.isClientResizable()) {
        val mode = if (it.player().getVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT) == 0) DisplayMode.RESIZABLE_NORMAL else DisplayMode.RESIZABLE_LIST
        p.toggleDisplayInterface(mode)
    }
}

onButton(parent = OptionsTab.ADVANCED_COMPONENT_ID, child = 23) {
    it.player().toggleVarbit(OSRSGameframe.CLOSE_TABS_WITH_HOTKEY_VARBIT)
}