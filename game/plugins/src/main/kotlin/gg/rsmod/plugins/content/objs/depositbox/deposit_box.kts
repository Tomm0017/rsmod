package gg.rsmod.plugins.content.objs.depositbox

import com.google.common.collect.ImmutableSet

val DEPOSIT_INTERFACE_ID = 192
val DEPOSIT_EQUIPMENT_SFX = 2238

val DEPOSIT_BOXES = ImmutableSet.of(Objs.BANK_DEPOSIT_BOX, Objs.BANK_DEPOSIT_BOX_25937, Objs.BANK_DEPOSIT_BOX_26254,
        Objs.BANK_DEPOSIT_BOX_29103, Objs.BANK_DEPOSIT_BOX_29104, Objs.BANK_DEPOSIT_BOX_29105, Objs.BANK_DEPOSIT_BOX_29106,
        Objs.BANK_DEPOSIT_BOX_29327, Objs.BANK_DEPOSIT_BOX_30268, Objs.BANK_DEPOSIT_BOX_31726, Objs.BANK_DEPOSIT_BOX_32665,
        Objs.BANK_DEPOSIT_BOX_34344)

DEPOSIT_BOXES.forEach { box ->
    on_obj_option(obj = box, option = "deposit") {
        open_deposit_box(player)
    }
}

on_button(interfaceId = DEPOSIT_INTERFACE_ID, component = 2) {
    val slot = player.getInteractingSlot()
    val opt = player.getInteractingOption()
    when (opt) {
        2 -> deposit_item(player, slot, 1)
        3 -> deposit_item(player, slot, 5)
        4 -> deposit_item(player, slot, 10)
        5 -> deposit_item(player, slot, Int.MAX_VALUE)
        10 -> {
            player.inventory[slot]?.let { item ->
                player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
        }
    }
}

on_button(interfaceId = DEPOSIT_INTERFACE_ID, component = 4) {
    val player = player
    deposit_all(player, player.inventory)
}

on_button(interfaceId = DEPOSIT_INTERFACE_ID, component = 6) {
    val player = player
    deposit_all(player, player.equipment, sound = DEPOSIT_EQUIPMENT_SFX)
}

fun open_deposit_box(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = DEPOSIT_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    p.setInterfaceEvents(interfaceId = DEPOSIT_INTERFACE_ID, component = 2, range = 0..27, setting = 1180734)
}

fun deposit_item(p: Player, slot: Int, amt: Int) {
    val item = p.inventory[slot] ?: return
    val amount = Math.min(p.inventory.getItemCount(item.id), amt)

    val add = p.bank.add(item.id, amount, assureFullInsertion = false)
    if (add.completed == 0) {
        p.message("Bank full.")
        return
    }

    val remove = p.inventory.remove(item.id, add.completed, assureFullRemoval = true)
    if (remove.hasFailed()) {
        add.items.forEach { p.bank.remove(it.item, beginSlot = it.slot) }
    }
}

fun deposit_all(p: Player, container: ItemContainer, sound: Int = -1) {
    var any = false

    if (container.isEmpty) {
        p.message("You have nothing to deposit.")
        return
    }

    container.filterNotNull().forEach { item ->
        val add = p.bank.add(item, assureFullInsertion = false)
        if (add.completed == 0) {
            return@forEach
        }

        val remove = container.remove(item.id, add.completed, assureFullRemoval = true)
        if (remove.hasFailed()) {
            add.items.forEach { p.bank.remove(it.item, beginSlot = it.slot) }
        } else {
            any = true
        }
    }

    if (!any) {
        p.message("Bank full.")
    } else if (sound != -1) {
        p.playSound(sound)
    }
}