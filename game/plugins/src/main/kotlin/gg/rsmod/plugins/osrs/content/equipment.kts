
import gg.rsmod.game.action.EquipAction
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

//bindUnequip(Equipment.WEAPON)

fun bindUnequip(equipment: Equipment, child: Int) {
    r.bindButton(parent = 387, child = child) {
        EquipAction.unequip(it.player(), equipment.id)
    }
}