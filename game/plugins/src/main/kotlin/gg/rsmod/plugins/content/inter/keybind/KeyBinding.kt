package gg.rsmod.plugins.content.inter.keybind

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.setVarbit

/**
 * @author Tom <rspsmods@gmail.com>
 */
object KeyBinding {

    const val FOCUSED_HOTKEY_VARBIT = 4690
    const val ESC_CLOSES_INTERFACES = 4681

    private fun disableAny(p: Player, hotkeyValue: Int) {
        Hotkey.values.forEach { hotkey ->
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