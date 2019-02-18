package gg.rsmod.plugins.content.inter.equipstats

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.osrs.api.ext.*

fun bind_unequip(equipment: EquipmentType, child: Int) {
    on_button(interfaceId = EquipmentStats.INTERFACE_ID, component = child) {
        val p = it.player()
        val opt = it.getInteractingOption()

        if (opt == 1) {
            EquipAction.unequip(p, equipment.id)
            p.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(p)
        } else if (opt == 10) {
            val item = p.equipment[equipment.id] ?: return@on_button
            p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
        } else {
            val item = p.equipment[equipment.id] ?: return@on_button
            if (!p.world.plugins.executeItem(p, item.id, opt)) {
                val slot = it.getInteractingSlot()
                if (p.world.devContext.debugButtons) {
                    p.message("Unhandled button action: [parent=84, child=$child, option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

on_button(interfaceId = EquipmentStats.TAB_INTERFACE_ID, component = 0) {
    val p = it.player()

    val slot = it.getInteractingSlot()
    val opt = it.getInteractingOption()
    val item = p.inventory[slot] ?: return@on_button

    if (opt == 1) {
        val result = EquipAction.equip(p, item, inventorySlot = slot)
        if (result == EquipAction.Result.SUCCESS) {
            p.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(p)
        } else if (result == EquipAction.Result.UNHANDLED) {
            p.message("You can't equip that.")
        }
    } else if (opt == 10) {
        p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
    }
}

on_button(interfaceId = 387, component = 17) {
    val p = it.player()

    if (!p.lock.canInterfaceInteract()) {
        return@on_button
    }

    p.setInterfaceUnderlay(-1, -1)
    p.openInterface(interfaceId = EquipmentStats.INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    p.openInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
    p.runClientScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    p.setInterfaceEvents(interfaceId = EquipmentStats.TAB_INTERFACE_ID, component = 0, range = 0..27, setting = 1180674)

    EquipmentStats.sendBonuses(p)
}

on_interface_close(interfaceId = EquipmentStats.INTERFACE_ID) {
    it.player().closeInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID)
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