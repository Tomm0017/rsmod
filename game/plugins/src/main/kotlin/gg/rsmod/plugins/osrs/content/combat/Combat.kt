package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.osrs.api.Equipment

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Combat {

    val ATTACK_DELAY = TimerKey()

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindEquipSlot(Equipment.WEAPON.id) {
            // Remove autocast?
        }
    }
}