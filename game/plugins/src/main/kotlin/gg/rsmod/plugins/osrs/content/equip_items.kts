
import gg.rsmod.plugins.osrs.api.Equipment
import gg.rsmod.plugins.playSound
import gg.rsmod.plugins.player

/**
 * @author Tom <rspsmods@gmail.com>
 */

val equipItemSound = 2238

for (equipment in Equipment.values()) {
    r.bindEquipSlot(equipment.id) {
        val p = it.player()
        p.playSound(equipItemSound)
    }
}