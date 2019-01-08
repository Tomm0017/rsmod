package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.osrs.model.Equipment
import gg.rsmod.plugins.player
import gg.rsmod.plugins.setVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialEnergy {

    const val ENERGY_VARP = 300
    const val ENABLED_VARP = 301

    fun setEnergy(p: Player, amount: Int) {
        check(amount in 0..100)
        p.setVarp(ENERGY_VARP, amount * 10)
    }

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindEquipSlot(Equipment.WEAPON.id) {
            it.player().setVarp(ENABLED_VARP, 0)
        }
    }
}