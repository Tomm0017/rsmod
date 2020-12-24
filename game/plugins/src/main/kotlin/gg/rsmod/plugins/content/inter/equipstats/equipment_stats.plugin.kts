package gg.rsmod.plugins.content.inter.equipstats

import gg.rsmod.plugins.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.EQUIPMENTSTATS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.EQUIPMENTSTATS_TAB_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.sendBonuses
import gg.rsmod.game.action.EquipAction

fun bind_unequip(equipment: EquipmentType, component: Int) {
    on_button(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = component) {
        val opt = player.getInteractingOption()

        if (opt == 1) {
            EquipAction.unequip(player, equipment.id)
            player.calculateBonuses()
            sendBonuses(player)
        } else if (opt == 10) {
            val item = player.equipment[equipment.id] ?: return@on_button
            world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        } else {
            val item = player.equipment[equipment.id] ?: return@on_button
            if (!world.plugins.executeItem(player, item.id, opt)) {
                val slot = player.getInteractingSlot()
                if (world.devContext.debugButtons) {
                    player.message("Unhandled button action: [component=[${EQUIPMENTSTATS_INTERFACE_ID}:$component], option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

on_button(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, component = 0) {
    val slot = player.getInteractingSlot()
    val opt = player.getInteractingOption()
    val item = player.inventory[slot] ?: return@on_button

    if (opt == 1) {
        val result = EquipAction.equip(player, item, inventorySlot = slot)
        if (result == EquipAction.Result.SUCCESS) {
            player.calculateBonuses()
            sendBonuses(player)
        } else if (result == EquipAction.Result.UNHANDLED) {
            player.message("You can't equip that.")
        }
    } else if (opt == 10) {
        world.sendExamine(player, item.id, ExamineEntityType.ITEM)
    }
}

on_button(interfaceId = EQUIPMENT_INTERFACE_ID, component = 1) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }

    player.setInterfaceUnderlay(-1, -1)
    player.openInterface(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    player.openInterface(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
    player.runClientScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    player.setInterfaceEvents(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, component = 0, range = 0..27, setting = 1180674)

    sendBonuses(player)
}

on_interface_close(interfaceId = EQUIPMENTSTATS_INTERFACE_ID) {
    player.closeInterface(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID)
}

bind_unequip(EquipmentType.HEAD, component = 10)
bind_unequip(EquipmentType.CAPE, component = 11)
bind_unequip(EquipmentType.AMULET, component = 12)
bind_unequip(EquipmentType.AMMO, component = 20)
bind_unequip(EquipmentType.WEAPON, component = 13)
bind_unequip(EquipmentType.CHEST, component = 14)
bind_unequip(EquipmentType.SHIELD, component = 15)
bind_unequip(EquipmentType.LEGS, component = 16)
bind_unequip(EquipmentType.GLOVES, component = 17)
bind_unequip(EquipmentType.BOOTS, component = 18)
bind_unequip(EquipmentType.RING, component = 19)