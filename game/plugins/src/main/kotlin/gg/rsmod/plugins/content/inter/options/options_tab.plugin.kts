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

fun bind_advanced_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

on_login {
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 6, range = 1..4, setting = 2) // Player option priority
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 7, range = 1..4, setting = 2) // Npc option priority
}

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
 * Screen brightness.
 */
for (offset in 0..3) {
    bind_setting(child = 18 + offset) {
        player.setVarp(OSRSGameframe.SCREEN_BRIGHTNESS_VARP, offset + 1)
    }
}

/**
 * Changing display modes (fixed, resizable).
 */
on_login {
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 40, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 42, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 44, range = 0..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 35, range = 1..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 36, range = 1..4, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 37, range = 1..3, setting = 2)
    player.setInterfaceEvents(interfaceId = OPTIONS_INTERFACE_ID, component = 115, range = 0..3, setting = 2)
}

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

/**
 * Advanced options.
 */
bind_setting(child = ALL_SETTINGS_BUTTON_ID) {
    if (!player.lock.canInterfaceInteract()) {
        return@bind_setting
    }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(interfaceId = ALL_SETTINGS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
}

on_button(ALL_SETTINGS_INTERFACE_ID, 4) {
    player.closeInterface(ALL_SETTINGS_INTERFACE_ID)
}

/**
 * Music volume.
 */
for (offset in 0..4) {
    bind_setting(child = 45 + offset) {
        player.setVarp(OSRSGameframe.MUSIC_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Sound effect volume.
 */
for (offset in 0..4) {
    bind_setting(child = 51 + offset) {
        player.setVarp(OSRSGameframe.SFX_VOLUME_VARP, Math.abs(offset - 4))
    }
}
/**
 * Area of sound effect volume.
 */
for (offset in 0..4) {
    bind_setting(child = 57 + offset) {
        player.setVarp(OSRSGameframe.ASX_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Toggle chat effects.
 */
bind_setting(child = 63) {
    player.toggleVarp(OSRSGameframe.CHAT_EFFECTS_VARP)
}

/**
 * Toggle split private chat.
 */
bind_setting(child = 65) {
    player.toggleVarp(OSRSGameframe.SPLIT_PRIVATE_VARP)
    player.runClientScript(83)
}

/**
 * Hide private messages when chat hidden.
 */
bind_setting(child = 67) {
    if (!player.isClientResizable() || player.getVarp(OSRSGameframe.SPLIT_PRIVATE_VARP) == 0) {
        player.message("That option is applicable only in resizable mode with 'Split Private Chat' enabled.")
    } else {
        player.toggleVarbit(OSRSGameframe.HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT)
    }
}

/**
 * Toggle profanity filter.
 */
bind_setting(child = 69) {
    player.toggleVarbit(OSRSGameframe.PROFANITY_VARP)
}

/**
 * Toggle idle timeout notification.
 */
bind_setting(child = 73) {
    player.toggleVarbit(OSRSGameframe.IDLE_NOTIFICATION_VARBIT)
}

/**
 * Toggle number of mouse buttons.
 */
bind_setting(child = 77) {
    player.toggleVarp(OSRSGameframe.MOUSE_BUTTONS_VARP)
}

/**
 * Toggle mouse camera.
 */
bind_setting(child = 79) {
    player.toggleVarbit(OSRSGameframe.MOUSE_CAMERA_VARBIT)
}

/**
 * Toggle follower (pet) options.
 */
bind_setting(child = 81) {
    player.toggleVarbit(OSRSGameframe.PET_OPTIONS_VARBIT)
}

/**
 * Set hotkey binds.
 */
bind_setting(child = 83) {
    if (!player.lock.canInterfaceInteract()) {
        return@bind_setting
    }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.setInterfaceEvents(interfaceId = 121, component = 111, range = 0..13, setting = 2)
    player.openInterface(interfaceId = 121, dest = InterfaceDestination.MAIN_SCREEN)
}

/**
 * Toggle shift-click dropping is an advanced setting under an entire tab.
 */
bind_advanced_setting(child = 17) {
    when(player.getInteractingSlot()) {
        30 -> player.toggleVarbit(OSRSGameframe.SHIFT_CLICK_DROP_VARBIT)
        else -> return@bind_advanced_setting
    }
}

/**
 * Set player option priority.
 */
bind_setting(child = 6) {
    val slot = player.getInteractingSlot()
    player.setVarp(OSRSGameframe.PLAYER_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Set npc option priority.
 */
bind_setting(child = 7) {
    val slot = player.getInteractingSlot()
    player.setVarp(OSRSGameframe.NPC_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Toggle aid.
 */
bind_setting(child = 26) {
    player.toggleVarp(OSRSGameframe.ACCEPT_AID_VARP)
}