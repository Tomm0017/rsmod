
import gg.rsmod.game.action.EquipAction
import gg.rsmod.plugins.osrs.api.Equipment
import gg.rsmod.plugins.playSound
import gg.rsmod.plugins.player
import gg.rsmod.plugins.sendWeaponInterfaceInformation

/**
 * @author Tom <rspsmods@gmail.com>
 */

val equipItemSound = 2238

for (equipment in Equipment.values()) {
    r.bindEquipSlot(equipment.id) {
        val p = it.player()

        p.playSound(equipItemSound)
        if (equipment == Equipment.WEAPON) {
            p.sendWeaponInterfaceInformation()
        }
    }
}

bindUnequip(Equipment.HEAD, child = 6)
bindUnequip(Equipment.CAPE, child = 7)
bindUnequip(Equipment.AMULET, child = 8)
bindUnequip(Equipment.AMMO, child = 16)
bindUnequip(Equipment.WEAPON, child = 9)
bindUnequip(Equipment.CHEST, child = 10)
bindUnequip(Equipment.SHIELD, child = 11)
bindUnequip(Equipment.LEGS, child = 12)
bindUnequip(Equipment.GLOVES, child = 13)
bindUnequip(Equipment.BOOTS, child = 14)
bindUnequip(Equipment.RING, child = 15)

fun bindUnequip(equipment: Equipment, child: Int) {
    r.bindButton(parent = 387, child = child) {
        val p = it.player()

        val result = EquipAction.unequip(p, equipment.id)
        if (equipment == Equipment.WEAPON && result == EquipAction.Result.SUCCESS) {
            p.sendWeaponInterfaceInformation()
        }
    }
}