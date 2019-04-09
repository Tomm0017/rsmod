package gg.rsmod.plugins.content.mechanics.equipment

import gg.rsmod.game.action.EquipAction

val EQUIP_ITEM_SOUND = 2238

fun bind_unequip(equipment: EquipmentType, child: Int) {
    on_button(interfaceId = 387, component = child) {
        val opt = player.getInteractingOption()
        when (opt) {
            1 -> {
                val result = EquipAction.unequip(player, equipment.id)
                if (equipment == EquipmentType.WEAPON && result == EquipAction.Result.SUCCESS) {
                    player.sendWeaponComponentInformation()
                }
            }
            10 -> {
                val item = player.equipment[equipment.id] ?: return@on_button
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
        }
    }
}

for (equipment in EquipmentType.values) {
    on_equip_to_slot(equipment.id) {
        player.playSound(EQUIP_ITEM_SOUND)
        if (equipment == EquipmentType.WEAPON) {
            player.sendWeaponComponentInformation()
        }
    }
}

bind_unequip(EquipmentType.HEAD, child = 6)
bind_unequip(EquipmentType.CAPE, child = 7)
bind_unequip(EquipmentType.AMULET, child = 8)
bind_unequip(EquipmentType.AMMO, child = 16)
bind_unequip(EquipmentType.WEAPON, child = 9)
bind_unequip(EquipmentType.CHEST, child = 10)
bind_unequip(EquipmentType.SHIELD, child = 11)
bind_unequip(EquipmentType.LEGS, child = 12)
bind_unequip(EquipmentType.GLOVES, child = 13)
bind_unequip(EquipmentType.BOOTS, child = 14)
bind_unequip(EquipmentType.RING, child = 15)
