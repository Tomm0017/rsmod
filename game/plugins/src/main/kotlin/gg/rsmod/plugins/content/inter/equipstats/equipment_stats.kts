package gg.rsmod.plugins.content.inter.equipstats

import gg.rsmod.game.action.EquipAction

fun bind_unequip(equipment: EquipmentType, child: Int) {
    on_button(interfaceId = EquipmentStats.INTERFACE_ID, component = child) {
        val opt = player.getInteractingOption()

        if (opt == 1) {
            EquipAction.unequip(player, equipment.id)
            player.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(player)
        } else if (opt == 10) {
            val item = player.equipment[equipment.id] ?: return@on_button
            world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        } else {
            val item = player.equipment[equipment.id] ?: return@on_button
            if (!world.plugins.executeItem(player, item.id, opt)) {
                val slot = player.getInteractingSlot()
                if (world.devContext.debugButtons) {
                    player.message("Unhandled button action: [parent=84, child=$child, option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

on_button(interfaceId = EquipmentStats.TAB_INTERFACE_ID, component = 0) {
    val slot = player.getInteractingSlot()
    val opt = player.getInteractingOption()
    val item = player.inventory[slot] ?: return@on_button

    if (opt == 1) {
        val result = EquipAction.equip(player, item, inventorySlot = slot)
        if (result == EquipAction.Result.SUCCESS) {
            player.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(player)
        } else if (result == EquipAction.Result.UNHANDLED) {
            player.message("You can't equip that.")
        }
    } else if (opt == 10) {
        world.sendExamine(player, item.id, ExamineEntityType.ITEM)
    }
}

on_button(interfaceId = 387, component = 17) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }

    player.setInterfaceUnderlay(-1, -1)
    player.openInterface(interfaceId = EquipmentStats.INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    player.openInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
    player.runClientScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    player.setInterfaceEvents(interfaceId = EquipmentStats.TAB_INTERFACE_ID, component = 0, range = 0..27, setting = 1180674)

    EquipmentStats.sendBonuses(player)
}

on_interface_close(interfaceId = EquipmentStats.INTERFACE_ID) {
    player.closeInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID)
}

bind_unequip(EquipmentType.HEAD, child = 11)
bind_unequip(EquipmentType.CAPE, child = 12)
bind_unequip(EquipmentType.AMULET, child = 13)
bind_unequip(EquipmentType.AMMO, child = 21)
bind_unequip(EquipmentType.WEAPON, child = 14)
bind_unequip(EquipmentType.CHEST, child = 15)
bind_unequip(EquipmentType.SHIELD, child = 16)
bind_unequip(EquipmentType.LEGS, child = 17)
bind_unequip(EquipmentType.GLOVES, child = 18)
bind_unequip(EquipmentType.BOOTS, child = 19)
bind_unequip(EquipmentType.RING, child = 20)