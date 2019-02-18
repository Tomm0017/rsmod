package gg.rsmod.plugins.content.inter.keybind

import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.osrs.api.ext.*

/**
 * Set 'focused' hotkey.
 */
Hotkey.values.forEach { hotkey ->
    on_button(interfaceId = 121, component = hotkey.child) {
        it.player().setVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT, hotkey.id)
    }
}

/**
 * Set hotkey value.
 */
on_button(interfaceId = 121, component = 111) {
    val p = it.player()
    val focused = p.getVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT)
    val hotkey = Hotkey.values.firstOrNull { h -> h.id == focused } ?: return@on_button

    val slot = it.getInteractingSlot()
    KeyBinding.set(p, hotkey, slot)
}

/**
 * Restore defaults.
 */
on_button(interfaceId = 121, component = 104) {
    val p = it.player()

    Hotkey.values.forEach { hotkey ->
        p.setVarbit(hotkey.varbit, hotkey.defaultValue)
    }
}

on_button(interfaceId = 121, component = 103) {
    it.player().toggleVarbit(KeyBinding.ESC_CLOSES_INTERFACES)
}