package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.osrs.api.Equipment
import gg.rsmod.plugins.playSound
import gg.rsmod.plugins.player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipItem {

    private const val EQUIP_ITEM_SOUND = 2238

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        for (equipment in Equipment.values()) {
            r.bindEquipSlot(equipment.id) {
                val p = it.player()

                p.playSound(EQUIP_ITEM_SOUND)
            }
        }
    }
}