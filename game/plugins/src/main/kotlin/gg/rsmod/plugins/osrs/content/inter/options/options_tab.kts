
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

fun bindSetting(child: Int, plugin: Function1<Plugin, Unit>) {
    r.bindButton(parent = OptionsTab.INTERFACE_ID, child = child) {
        plugin.invoke(it)
    }
}

/**
 * Toggle mouse scroll wheel zoom.
 */
r.bindButton(parent = OptionsTab.INTERFACE_ID, child = 5) {
    // TODO(Tom): figure out why this varbit isn't causing the cross to be drawn.
    // It technically works since it won't allow zooming with mouse wheel, but it
    // doesn't visually show on the interface.
    //
    //  NORMAL:  id=1055, state=467456
    //  CROSSED: id=1055, state=537338368
    it.player().toggleVarbit(OptionsTab.DISABLE_MOUSEWHEEL_ZOOM_VARBIT)
}

/**
 * Screen brightness.
 */
for (offset in 0..3) {
    bindSetting(child = 18 + offset) {
        it.player().setVarp(OptionsTab.SCREEN_BRIGHTNESS_VARP, offset + 1)
    }
}

/**
 * Music volume.
 */
for (offset in 0..4) {
    bindSetting(child = 45 + offset) {
        it.player().setVarp(OptionsTab.MUSIC_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Sound effect volume.
 */
for (offset in 0..4) {
    bindSetting(child = 51 + offset) {
        it.player().setVarp(OptionsTab.SFX_VOLUME_VARP, Math.abs(offset - 4))
    }
}
/**
 * Area of sound effect volume.
 */
for (offset in 0..4) {
    bindSetting(child = 57 + offset) {
        it.player().setVarp(OptionsTab.ASX_VOLUME_VARP, Math.abs(offset - 4))
    }
}

/**
 * Toggle chat effects.
 */
bindSetting(child = 63) {
    it.player().toggleVarp(OptionsTab.CHAT_EFFECTS_VARP)
}

/**
 * Toggle split private chat.
 */
bindSetting(child = 65) {
    val p = it.player()
    p.toggleVarp(OptionsTab.SPLIT_PRIVATE_VARP)
    p.invokeScript(83)
}

/**
 * Hide private messages when chat hidden.
 */
bindSetting(child = 67) {
    val p = it.player()
    if (!p.isClientResizable() || p.getVarp(OptionsTab.SPLIT_PRIVATE_VARP) == 0) {
        p.message("That option is applicable only in resizable mode with 'Split Private Chat' enabled.")
    } else {
        p.toggleVarbit(OptionsTab.HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT)
    }
}

/**
 * Toggle profanity filter.
 */
bindSetting(child = 69) {
    it.player().toggleVarbit(OptionsTab.PROFANITY_VARP)
}

/**
 * Toggle idle timeout notification.
 */
bindSetting(child = 73) {
    it.player().toggleVarbit(OptionsTab.IDLE_NOTIFICATION_VARBIT)
}

/**
 * Toggle number of mouse buttons.
 */
bindSetting(child = 77) {
    it.player().toggleVarp(OptionsTab.MOUSE_BUTTONS_VARP)
}

/**
 * Toggle mouse camera.
 */
bindSetting(child = 79) {
    it.player().toggleVarbit(OptionsTab.MOUSE_CAMERA_VARBIT)
}

/**
 * Toggle follower (pet) options.
 */
bindSetting(child = 81) {
    it.player().toggleVarbit(OptionsTab.PET_OPTIONS_VARBIT)
}

/**
 * Set hotkey binds.
 */
bindSetting(child = 83) {
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@bindSetting
    }
    p.setMainInterfaceBackground(color = -1, transparency = -1)
    p.setInterfaceSetting(parent = 121, child = 112, range = 0..13, setting = 2)
    p.openInterface(interfaceId = 121, pane = InterfacePane.MAIN_SCREEN)
}

/**
 * Toggle shift-click dropping.
 */
bindSetting(child = 85) {
    it.player().toggleVarbit(OptionsTab.SHIFT_CLICK_DROP_VARBIT)
}

/**
 * Set player option priority.
 */
bindSetting(child = 106) {
    val slot = it.getInteractingSlot()
    it.player().setVarp(OptionsTab.PLAYER_ATTACK_PRIORITY_VARP, slot)
}

/**
 * Set player option priority.
 */
bindSetting(child = 107) {
    val slot = it.getInteractingSlot()
    it.player().setVarp(OptionsTab.NPC_ATTACK_PRIORITY_VARP, slot)
}

/**
 * Toggle aid.
 */
bindSetting(child = 92) {
    it.player().toggleVarp(OptionsTab.ACCEPT_AID_VARP)
}