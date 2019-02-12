
import gg.rsmod.game.action.EquipAction
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.ext.playSound
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.sendWeaponComponentInformation

val EQUIP_ITEM_SOUND = 2238

for (equipment in EquipmentType.values()) {
    onEquipSlot(equipment.id) {
        val p = it.player()

        p.playSound(EQUIP_ITEM_SOUND)
        if (equipment == EquipmentType.WEAPON) {
            p.sendWeaponComponentInformation()
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
    onButton(parent = 387, child = child) {
        val p = it.player()

        val result = EquipAction.unequip(p, equipment.id)
        if (equipment == EquipmentType.WEAPON && result == EquipAction.Result.SUCCESS) {
            p.sendWeaponComponentInformation()
        }
    }
}