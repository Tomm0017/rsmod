package gg.rsmod.plugins.osrs.content.inter.keybind

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.ext.getVarbit
import gg.rsmod.plugins.osrs.api.ext.setVarbit

/**
 * @author Tom <rspsmods@gmail.com>
 */
object KeyBinding {

    const val FOCUSED_HOTKEY_VARBIT = 4690
    const val ESC_CLOSES_INTERFACES = 4681

    fun disableAny(p: Player, hotkeyValue: Int) {
        Hotkey.values().forEach { hotkey ->
            if (p.getVarbit(hotkey.varbit) == hotkeyValue) {
                p.setVarbit(hotkey.varbit, 0)
            }
        }
    }

    fun set(p: Player, hotkey: Hotkey, hotkeyValue: Int) {
        disableAny(p, hotkeyValue)
        p.setVarbit(hotkey.varbit, hotkeyValue)
    }
}