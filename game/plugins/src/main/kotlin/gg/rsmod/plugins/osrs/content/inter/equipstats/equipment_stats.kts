
import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.equipstats.EquipmentStats

fun bindUnequip(equipment: EquipmentType, child: Int) {
    onButton(parent = EquipmentStats.COMPONENT_ID, child = child) {
        val p = it.player()
        val opt = it.getInteractingOption()

        if (opt == 0) {
            EquipAction.unequip(p, equipment.id)
            p.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(p)
        } else if (opt == 9) {
            val item = p.equipment[equipment.id] ?: return@onButton
            p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
        } else {
            val item = p.equipment[equipment.id] ?: return@onButton
            if (!p.world.plugins.executeItem(p, item.id, opt)) {
                val slot = it.getInteractingSlot()
                if (p.world.devContext.debugButtons) {
                    p.message("Unhandled button action: [parent=84, child=$child, option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

onButton(parent = EquipmentStats.TAB_COMPONENT_ID, child = 0) {
    val p = it.player()

    val slot = it.getInteractingSlot()
    val opt = it.getInteractingOption()
    val item = p.inventory[slot] ?: return@onButton

    if (opt == 0) {
        val result = EquipAction.equip(p, item, inventorySlot = slot)
        if (result == EquipAction.Result.SUCCESS) {
            p.calculateWeightAndBonus(weight = false, bonuses = true)
            EquipmentStats.sendBonuses(p)
        } else if (result == EquipAction.Result.UNHANDLED) {
            p.message("You can't equip that.")
        }
    } else if (opt == 9) {
        p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
    }
}

onButton(parent = 387, child = 17) {
    val p = it.player()

    if (!p.lock.canComponentInteract()) {
        return@onButton
    }

    p.setComponentUnderlay(-1, -1)
    p.openComponent(component = EquipmentStats.COMPONENT_ID, pane = ComponentPane.MAIN_SCREEN)
    p.openComponent(component = EquipmentStats.TAB_COMPONENT_ID, pane = ComponentPane.TAB_AREA)
    p.invokeScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    p.setComponentSetting(parent = EquipmentStats.TAB_COMPONENT_ID, child = 0, range = 0..27, setting = 1180674)

    EquipmentStats.sendBonuses(p)
}

onComponentClose(component = EquipmentStats.COMPONENT_ID) {
    it.player().closeComponent(component = EquipmentStats.TAB_COMPONENT_ID)
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