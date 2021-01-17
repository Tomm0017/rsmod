package gg.rsmod.plugins.content.inter.options

import gg.rsmod.plugins.content.inter.options.OptionsTab.OPTIONS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.options.OptionsTab.ALL_SETTINGS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.options.OptionsTab.ALL_SETTINGS_BUTTON_ID
import gg.rsmod.game.model.interf.DisplayMode

fun bind_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OPTIONS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

on_login {
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 6, range = 1..4, setting = 2) // Player option priority
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 7, range = 1..4, setting = 2) // Npc option priority

    /**
     * Changing display modes (fixed, resizable).
     */
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 40, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 42, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 44, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 35, range = 1..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 36, range = 1..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 37, range = 1..3, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 115, range = 0..3, setting = 2)
}

/**
 * Advanced options.
 */
bind_setting(child = ALL_SETTINGS_BUTTON_ID) {
    if (!player.lock.canInterfaceInteract()) {
        return@bind_setting
    }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(interfaceId = ALL_SETTINGS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    player.setInterfaceEvents(interfaceId = ALL_SETTINGS_INTERFACE_ID, component = 22, range = 0..5, setting = 2)
    player.setInterfaceEvents(interfaceId = ALL_SETTINGS_INTERFACE_ID, component = 18, range = 0..87, setting = 2)
    player.setInterfaceEvents(interfaceId = ALL_SETTINGS_INTERFACE_ID, component = 27, range = 0..122, setting = 2)
    player.setInterfaceEvents(interfaceId = ALL_SETTINGS_INTERFACE_ID, component = 20, range = 0..28, setting = 2)
}

on_button(ALL_SETTINGS_INTERFACE_ID, Settings.SETTINGS_CLOSE_BUTTON_ID) {
    player.closeInterface(ALL_SETTINGS_INTERFACE_ID)
}

/** #################################################################################
 *                      CONTROLS
 ################################################################################# */

/**
 * Toggle mouse scroll wheel zoom.
 */
on_button(interfaceId = OPTIONS_INTERFACE_ID, component = 5) {
    // TODO(Tom): figure out why this varbit isn't causing the cross to be drawn.
    // It technically works since it won't allow zooming with mouse wheel, but it
    // doesn't visually show on the interface.
    //
    //  NORMAL:  id=1055, state=467456
    //  CROSSED: id=1055, state=537338368
    player.toggleVarbit(OSRSGameframe.DISABLE_MOUSEWHEEL_ZOOM_VARBIT)
}

/**
 * Zoom level setting
 */
bind_setting(child = 59) {
    player.message("zoom set to ${player.getInteractingSlot()}")
}

// slider
bind_setting(child = 60) {
    player.message("zoom set to ${player.getInteractingSlot()}")
}

/**
 * Set player option priority.
 */
bind_setting(child = 35) {
    val slot = player.getInteractingSlot()
    player.setVarp(OSRSGameframe.PLAYER_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Set npc option priority.
 */
bind_setting(child = 36) {
    val slot = player.getInteractingSlot()
    player.setVarp(OSRSGameframe.NPC_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Accept aid toggle.
 */
bind_setting(child = OptionsTab.ACCEPT_AID_BUTTON_ID) {
    player.toggleVarp(OSRSGameframe.ACCEPT_AID_VARBIT)
}

// Run toggle already handled by run_energy plugin

/** #################################################################################
 *                        AUDIO
################################################################################# */

/**
 * Music
 */

// toggle mute
bind_setting(child = 14) {
    player.message("toggle mute")
}

bind_setting(child = 40) {
    val slot = player.getInteractingSlot()
    player.message("Music volume set: $slot")
    player.setVarp(OSRSGameframe.MUSIC_VOLUME_VARP, Math.abs(slot - 4))
}

/**
 * Sound effects
 */
bind_setting(child = 42) {
    val slot = player.getInteractingSlot()
    player.message("SFX volume set: $slot")
    player.setVarp(OSRSGameframe.SFX_VOLUME_VARP, Math.abs(slot - 4))
}

/**
 * Area sounds
 */
bind_setting(child = 44) {
    val slot = player.getInteractingSlot()
    player.message("ASX volume set: $slot")
    player.setVarp(OSRSGameframe.ASX_VOLUME_VARP, Math.abs(slot - 4))
}

/** #################################################################################
 *                       DISPLAY
################################################################################# */

/**
 * Screen brightness.
 */
bind_setting(child = 115) {
    val slot = player.getInteractingSlot()
    player.message("Brightness set: $slot")
    player.setVarp(OSRSGameframe.SCREEN_BRIGHTNESS_VARP, slot + 1)
}

/**
 * Display mode dropdown.
 */
on_button(OPTIONS_INTERFACE_ID, 37) {
    val slot = player.getInteractingSlot()
    val mode = when (slot) {
        2 -> DisplayMode.RESIZABLE_NORMAL
        3 -> DisplayMode.RESIZABLE_LIST
        else -> DisplayMode.FIXED
    }
    player.toggleDisplayInterface(mode)
    player.runClientScript(3998, slot-1)
}
