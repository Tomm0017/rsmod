package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.interf.DisplayMode

fun bind_all_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

bind_all_setting(Settings.SETTINGS_TAB_SELECTOR) {
    player.message("selected tab ${player.getInteractingSlot()}")
}

/**
 * Display Tab
 */

bind_all_setting(70000) {
    player.toggleVarbit(OSRSGameframe.CHATBOX_SCROLLBAR_VARBIT)
}

//bind_all_setting(6) {
//    player.toggleVarbit(OSRSGameframe.DISABLE_SIDEPANELS_OPAQUE_VARBIT)
//}
//
//bind_all_setting(8) {
//    player.toggleVarbit(OSRSGameframe.DISABLE_XP_TILL_LEVEL_VARBIT)
//}
//
//bind_all_setting(10) {
//    player.toggleVarbit(OSRSGameframe.DISABLE_PRAYER_TOOLTIP_VARBIT)
//}
//
//bind_all_setting(12) {
//    player.toggleVarbit(OSRSGameframe.DISABLE_SPECIAL_ATTACK_TOOLTIP_VARBIT)
//}
//
//bind_all_setting(16) {
//    player.toggleVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT)
//
//    /**
//     * Close or open the XP drop interface.
//     */
//    if (player.getVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT) == 0) {
//        player.openInterface(interfaceId = 160, dest = InterfaceDestination.MINI_MAP)
//    } else {
//        player.closeInterface(interfaceId = 160)
//    }
//}
//
///**
// * Toggle shift-click dropping is an advanced setting under an entire tab.
// */
//bind_all_setting(child = 17) {
//    when(player.getInteractingSlot()) {
//        30 -> player.toggleVarbit(OSRSGameframe.SHIFT_CLICK_DROP_VARBIT)
//        else -> return@bind_all_setting
//    }
//}
//
//bind_all_setting(18) {
//    player.toggleVarbit(OSRSGameframe.CHATBOX_TRANSPARENT_VARBIT)
//}
//
//bind_all_setting(20) {
//    player.toggleVarbit(OSRSGameframe.CHATBOX_SOLID_VARBIT)
//}
//
//bind_all_setting(21) {
//    player.toggleVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT)
//
//    if (player.isClientResizable()) {
//        val mode = if (player.getVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT) == 0) DisplayMode.RESIZABLE_NORMAL else DisplayMode.RESIZABLE_LIST
//        player.toggleDisplayInterface(mode)
//    }
//}
//
//bind_all_setting(23) {
//    player.toggleVarbit(OSRSGameframe.CLOSE_TABS_WITH_HOTKEY_VARBIT)
//}
//
//
///**
// * Toggle chat effects.
// */
//bind_all_setting(child = 63) {
//    player.toggleVarp(OSRSGameframe.CHAT_EFFECTS_VARP)
//}
//
///**
// * Toggle split private chat.
// */
//bind_all_setting(child = 65) {
//    player.toggleVarp(OSRSGameframe.SPLIT_PRIVATE_VARP)
//    player.runClientScript(83)
//}
//
///**
// * Hide private messages when chat hidden.
// */
//bind_all_setting(child = 67) {
//    if (!player.isClientResizable() || player.getVarp(OSRSGameframe.SPLIT_PRIVATE_VARP) == 0) {
//        player.message("That option is applicable only in resizable mode with 'Split Private Chat' enabled.")
//    } else {
//        player.toggleVarbit(OSRSGameframe.HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT)
//    }
//}
//
///**
// * Toggle profanity filter.
// */
//bind_all_setting(child = 69) {
//    player.toggleVarbit(OSRSGameframe.PROFANITY_VARP)
//}
//
///**
// * Toggle idle timeout notification.
// */
//bind_all_setting(child = 73) {
//    player.toggleVarbit(OSRSGameframe.IDLE_NOTIFICATION_VARBIT)
//}
//
///**
// * Toggle number of mouse buttons.
// */
//bind_all_setting(child = 77) {
//    player.toggleVarp(OSRSGameframe.MOUSE_BUTTONS_VARP)
//}
//
///**
// * Toggle mouse camera.
// */
//bind_all_setting(child = 79) {
//    player.toggleVarbit(OSRSGameframe.MOUSE_CAMERA_VARBIT)
//}
//
///**
// * Toggle follower (pet) options.
// */
//bind_all_setting(child = 81) {
//    player.toggleVarbit(OSRSGameframe.PET_OPTIONS_VARBIT)
//}
//
///**
// * Set hotkey binds.
// */
//bind_all_setting(child = 83) {
//    if (!player.lock.canInterfaceInteract()) {
//        return@bind_all_setting
//    }
//    player.setInterfaceUnderlay(color = -1, transparency = -1)
//    player.setInterfaceEvents(interfaceId = 121, component = 111, range = 0..13, setting = 2)
//    player.openInterface(interfaceId = 121, dest = InterfaceDestination.MAIN_SCREEN)
//}