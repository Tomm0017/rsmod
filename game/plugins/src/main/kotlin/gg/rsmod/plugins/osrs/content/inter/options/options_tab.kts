package gg.rsmod.plugins.osrs.content.inter.options

import gg.rsmod.game.model.DISPLAY_MODE_CHANGE_ATTR
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.OSRSGameframe
import gg.rsmod.plugins.osrs.api.ext.*

fun bind_setting(child: Int, plugin: Function1<Plugin, Unit>) {
    on_button(parent = OptionsTab.INTERFACE_ID, child = child) {
        plugin.invoke(it)
    }
}

on_login {
    val p = it.player()

    p.setInterfaceEvents(parent = OptionsTab.INTERFACE_ID, child = 106, range = 1..4, setting = 2) // Player option priority
    p.setInterfaceEvents(parent = OptionsTab.INTERFACE_ID, child = 107, range = 1..4, setting = 2) // Npc option priority
}

/**
 * Toggle mouse scroll wheel zoom.
 */
on_button(parent = OptionsTab.INTERFACE_ID, child = 5) {
    // TODO(Tom): figure out why this varbit isn't causing the cross to be drawn.
    // It technically works since it won't allow zooming with mouse wheel, but it
    // doesn't visually show on the interface.
    //
    //  NORMAL:  id=1055, state=467456
    //  CROSSED: id=1055, state=537338368
    it.player().toggleVarbit(OSRSGameframe.DISABLE_MOUSEWHEEL_ZOOM_VARBIT)
}

/**
 * Screen brightness.
 */
for (offset in 0..3) {
    bind_setting(child = 18 + offset) {
        it.player().setVarp(OSRSGameframe.SCREEN_BRIGHTNESS_VARP, offset + 1)
    }
}

/**
 * Changing display modes (fixed, resizable).
 */
on_display_change {
    val p = it.player()
    val change = p.attr[DISPLAY_MODE_CHANGE_ATTR]
    val mode = when (change) {
        2 -> if (p.getVarbit(OSRSGameframe.SIDESTONES_ARRAGEMENT_VARBIT) == 1) DisplayMode.RESIZABLE_LIST else DisplayMode.RESIZABLE_NORMAL
        else -> DisplayMode.FIXED
    }
    p.toggleDisplayInterface(mode)
}

/**
 * Advanced options.
 */
bind_setting(child = 35) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@bind_setting
    }
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(component = OptionsTab.ADVANCED_COMPONENT_ID, pane = InterfaceDestination.MAIN_SCREEN)
}

/**
 * Music volume.
 */
for (offset in 0..4) {
    bind_setting(child = 45 + offset) {
        it.player().setVarp(OSRSGameframe.MUSIC_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Sound effect volume.
 */
for (offset in 0..4) {
    bind_setting(child = 51 + offset) {
        it.player().setVarp(OSRSGameframe.SFX_VOLUME_VARP, Math.abs(offset - 4))
    }
}
/**
 * Area of sound effect volume.
 */
for (offset in 0..4) {
    bind_setting(child = 57 + offset) {
        it.player().setVarp(OSRSGameframe.ASX_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Toggle chat effects.
 */
bind_setting(child = 63) {
    it.player().toggleVarp(OSRSGameframe.CHAT_EFFECTS_VARP)
}

/**
 * Toggle split private chat.
 */
bind_setting(child = 65) {
    val p = it.player()
    p.toggleVarp(OSRSGameframe.SPLIT_PRIVATE_VARP)
    p.runClientScript(83)
}

/**
 * Hide private messages when chat hidden.
 */
bind_setting(child = 67) {
    val p = it.player()
    if (!p.isClientResizable() || p.getVarp(OSRSGameframe.SPLIT_PRIVATE_VARP) == 0) {
        p.message("That option is applicable only in resizable mode with 'Split Private Chat' enabled.")
    } else {
        p.toggleVarbit(OSRSGameframe.HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT)
    }
}

/**
 * Toggle profanity filter.
 */
bind_setting(child = 69) {
    it.player().toggleVarbit(OSRSGameframe.PROFANITY_VARP)
}

/**
 * Toggle idle timeout notification.
 */
bind_setting(child = 73) {
    it.player().toggleVarbit(OSRSGameframe.IDLE_NOTIFICATION_VARBIT)
}

/**
 * Toggle number of mouse buttons.
 */
bind_setting(child = 77) {
    it.player().toggleVarp(OSRSGameframe.MOUSE_BUTTONS_VARP)
}

/**
 * Toggle mouse camera.
 */
bind_setting(child = 79) {
    it.player().toggleVarbit(OSRSGameframe.MOUSE_CAMERA_VARBIT)
}

/**
 * Toggle follower (pet) options.
 */
bind_setting(child = 81) {
    it.player().toggleVarbit(OSRSGameframe.PET_OPTIONS_VARBIT)
}

/**
 * Set hotkey binds.
 */
bind_setting(child = 83) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@bind_setting
    }
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.setInterfaceEvents(parent = 121, child = 111, range = 0..13, setting = 2)
    p.openInterface(component = 121, pane = InterfaceDestination.MAIN_SCREEN)
}

/**
 * Toggle shift-click dropping.
 */
bind_setting(child = 85) {
    it.player().toggleVarbit(OSRSGameframe.SHIFT_CLICK_DROP_VARBIT)
}

/**
 * Set player option priority.
 */
bind_setting(child = 106) {
    val slot = it.getInteractingSlot()
    it.player().setVarp(OSRSGameframe.PLAYER_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Set npc option priority.
 */
bind_setting(child = 107) {
    val slot = it.getInteractingSlot()
    it.player().setVarp(OSRSGameframe.NPC_ATTACK_PRIORITY_VARP, slot - 1)
}

/**
 * Toggle aid.
 */
bind_setting(child = 92) {
    it.player().toggleVarp(OSRSGameframe.ACCEPT_AID_VARP)
}