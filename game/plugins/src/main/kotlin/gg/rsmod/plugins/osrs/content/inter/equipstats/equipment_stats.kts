
import gg.rsmod.game.action.EquipAction
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.equipstats.EquipmentStats

fun bindUnequip(equipment: EquipmentType, child: Int) {
    r.bindButton(parent = EquipmentStats.INTERFACE_ID, child = child) {
        val p = it.player()
        val opt = it.getInteractingOption()

        if (opt == 0) {
            EquipAction.unequip(p, equipment.id)
            p.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(p)
        } else if (opt == 9) {
            // TODO(Tom): send examine
        } else {
            val item = p.equipment[equipment.id] ?: return@bindButton
            if (!p.world.plugins.executeItem(p, item.id, opt)) {
                val slot = it.getInteractingSlot()
                if (p.world.devContext.debugButtons) {
                    p.message("Unhandled button action: [parent=84, child=$child, option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

r.bindButton(parent = EquipmentStats.TAB_INTERFACE_ID, child = 0) {
    val p = it.player()

    val slot = it.getInteractingSlot()
    val item = p.inventory[slot] ?: return@bindButton

    val result = EquipAction.equip(p, item, inventorySlot = slot)
    if (result == EquipAction.Result.SUCCESS) {
        p.calculateWeightAndBonus(weight = false, bonuses = true)
        EquipmentStats.sendBonuses(p)
    } else if (result == EquipAction.Result.UNHANDLED) {
        p.message("You can't equip that.")
    }
}

r.bindButton(parent = 387, child = 17) {
    val p = it.player()

    if (!p.lock.canInterfaceInteract()) {
        return@bindButton
    }

    p.setMainInterfaceBackground(-1, -1)
    p.openInterface(interfaceId = EquipmentStats.INTERFACE_ID, pane = InterfacePane.MAIN_SCREEN)
    p.openInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID, pane = InterfacePane.TAB_AREA)
    p.invokeScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    p.setInterfaceSetting(parent = EquipmentStats.TAB_INTERFACE_ID, child = 0, range = 0..27, setting = 1180674)

    EquipmentStats.sendBonuses(p)
}

r.bindInterfaceClose(parent = EquipmentStats.INTERFACE_ID) {
    it.player().closeInterface(interfaceId = EquipmentStats.TAB_INTERFACE_ID)
}

bindUnequip(EquipmentType.HEAD, child = 11)
bindUnequip(EquipmentType.CAPE, child = 12)
bindUnequip(EquipmentType.AMULET, child = 13)
bindUnequip(EquipmentType.AMMO, child = 21)
bindUnequip(EquipmentType.WEAPON, child = 14)
bindUnequip(EquipmentType.CHEST, child = 15)
bindUnequip(EquipmentType.SHIELD, child = 16)
bindUnequip(EquipmentType.LEGS, child = 17)
bindUnequip(EquipmentType.GLOVES, child = 18)
bindUnequip(EquipmentType.BOOTS, child = 19)
bindUnequip(EquipmentType.RING, child = 20)