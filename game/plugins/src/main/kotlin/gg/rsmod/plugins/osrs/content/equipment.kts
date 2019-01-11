
import gg.rsmod.game.action.EquipAction
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.playSound
import gg.rsmod.plugins.player
import gg.rsmod.plugins.sendWeaponInterfaceInformation

/**
 * @author Tom <rspsmods@gmail.com>
 */

val equipItemSound = 2238

for (equipment in EquipmentType.values()) {
    r.bindEquipSlot(equipment.id) {
        val p = it.player()

        p.playSound(equipItemSound)
        if (equipment == EquipmentType.WEAPON) {
            p.sendWeaponInterfaceInformation()
        }
    }
}

bindUnequip(EquipmentType.HEAD, child = 6)
bindUnequip(EquipmentType.CAPE, child = 7)
bindUnequip(EquipmentType.AMULET, child = 8)
bindUnequip(EquipmentType.AMMO, child = 16)
bindUnequip(EquipmentType.WEAPON, child = 9)
bindUnequip(EquipmentType.CHEST, child = 10)
bindUnequip(EquipmentType.SHIELD, child = 11)
bindUnequip(EquipmentType.LEGS, child = 12)
bindUnequip(EquipmentType.GLOVES, child = 13)
bindUnequip(EquipmentType.BOOTS, child = 14)
bindUnequip(EquipmentType.RING, child = 15)

fun bindUnequip(equipment: EquipmentType, child: Int) {
    r.bindButton(parent = 387, child = child) {
        val p = it.player()

        val result = EquipAction.unequip(p, equipment.id)
        if (equipment == EquipmentType.WEAPON && result == EquipAction.Result.SUCCESS) {
            p.sendWeaponInterfaceInformation()
        }
    }
}