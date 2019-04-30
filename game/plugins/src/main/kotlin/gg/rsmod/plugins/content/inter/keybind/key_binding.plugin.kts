package gg.rsmod.plugins.content.inter.keybind

/**
 * Set 'focused' hotkey.
 */
Hotkey.values.forEach { hotkey ->
    on_button(interfaceId = 121, component = hotkey.child) {
        player.setVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT, hotkey.id)
    }
}

/**
 * Set hotkey value.
 */
on_button(interfaceId = 121, component = 111) {
    val focused = player.getVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT)
    val hotkey = Hotkey.values.firstOrNull { h -> h.id == focused } ?: return@on_button

    val slot = player.getInteractingSlot()
    KeyBinding.set(player, hotkey, slot)
}

/**
 * Restore defaults.
 */
on_button(interfaceId = 121, component = 104) {
    Hotkey.values.forEach { hotkey ->
        player.setVarbit(hotkey.varbit, hotkey.defaultValue)
    }
}

on_button(interfaceId = 121, component = 103) {
    player.toggleVarbit(KeyBinding.ESC_CLOSES_INTERFACES)
}