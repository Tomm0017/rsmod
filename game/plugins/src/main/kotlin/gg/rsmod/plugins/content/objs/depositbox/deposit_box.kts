package gg.rsmod.plugins.content.objs.depositbox

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR

private val DEPOSIT_INTERFACE_ID = 192
private val DEPOSIT_EQUIPMENT_SFX = 2238
private val DEPOSIT_ANIMATION = 834

private val DEPOSIT_BOXES = setOf(
        Objs.BANK_DEPOSIT_BOX, Objs.BANK_DEPOSIT_BOX_25937, Objs.BANK_DEPOSIT_BOX_26254,
        Objs.BANK_DEPOSIT_BOX_29103, Objs.BANK_DEPOSIT_BOX_29104, Objs.BANK_DEPOSIT_BOX_29105, Objs.BANK_DEPOSIT_BOX_29106,
        Objs.BANK_DEPOSIT_BOX_29327, Objs.BANK_DEPOSIT_BOX_30268, Objs.BANK_DEPOSIT_BOX_31726, Objs.BANK_DEPOSIT_BOX_32665,
        Objs.BANK_DEPOSIT_BOX_34344)

DEPOSIT_BOXES.forEach { box ->
    on_obj_option(obj = box, option = "deposit") {
        open_deposit_box(player)
    }
}

on_interface_close(DEPOSIT_INTERFACE_ID) {
    close_deposit_box(player)
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
    deposit_inv(player)
}

on_button(interfaceId = DEPOSIT_INTERFACE_ID, component = 6) {
    deposit_equipment(player)
}

on_component_to_component_item_swap(
        srcInterfaceId = DEPOSIT_INTERFACE_ID, srcComponent = 2,
        dstInterfaceId = DEPOSIT_INTERFACE_ID, dstComponent = 2) {
    val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
    val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!

    val container = player.inventory

    if (srcSlot in 0 until container.capacity && dstSlot in 0 until container.capacity) {
        container.swap(srcSlot, dstSlot)
    } else {
        // Sync the container on the client
        container.dirty = true
    }
}

fun open_deposit_box(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = DEPOSIT_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    p.setInterfaceEvents(interfaceId = DEPOSIT_INTERFACE_ID, component = 2, range = 0..27, setting = 1180734)
    p.closeInterface(InterfaceDestination.INVENTORY)
    p.closeInterface(InterfaceDestination.EQUIPMENT)
}

fun close_deposit_box(p: Player) {
    p.openInterface(InterfaceDestination.INVENTORY)
    p.openInterface(InterfaceDestination.EQUIPMENT)
}

fun deposit_item(p: Player, slot: Int, amt: Int) {
    val from = p.inventory
    val to = p.bank

    val item = from[slot] ?: return
    val amount = Math.min(from.getItemCount(item.id), amt)

    var deposited = 0
    for (i in 0 until from.capacity) {
        val inv = from[i] ?: continue
        if (inv.id != item.id) {
            continue
        }

        if (deposited >= amount) {
            break
        }

        val left = amount - deposited

        val copy = Item(inv.id, Math.min(left, inv.amount))
        if (copy.amount >= inv.amount) {
            copy.copyAttr(inv)
        }

        val placeholderSlot = to.removePlaceholder(p.world, copy)
        val transfer = from.transfer(to, item = copy, fromSlot = i, toSlot = placeholderSlot, note = false, unnote = true)

        if (transfer != null) {
            deposited += transfer.completed
        }
    }

    if (deposited == 0) {
        p.message("Bank full.")
    }

    p.animate(DEPOSIT_ANIMATION)
}

fun deposit_inv(player: Player) {
    val from = player.inventory
    val to = player.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount

        val placeholderSlot = to.removePlaceholder(world, item)
        val deposited = from.transfer(to, item, fromSlot = i, toSlot = placeholderSlot, note = false, unnote = true)?.completed ?: 0
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
        }
    }

    if (!any && !from.isEmpty) {
        player.message("Bank full.")
    }
}

fun deposit_equipment(player: Player) {
    val from = player.equipment
    val to = player.bank

    var any = false
    for (i in 0 until from.capacity) {
        val item = from[i] ?: continue

        val total = item.amount

        val placeholderSlot = to.removePlaceholder(world, item)
        val deposited = from.transfer(to, item, fromSlot = i, toSlot = placeholderSlot, note = false, unnote = true)?.completed ?: 0
        if (total != deposited) {
            // Was not able to deposit the whole stack of [item].
        }
        if (deposited > 0) {
            any = true
            EquipAction.onItemUnequip(player, item.id)
        }
    }

    if (!any && !from.isEmpty) {
        player.message("Bank full.")
    } else {
        player.playSound(DEPOSIT_EQUIPMENT_SFX)
    }
}

fun ItemContainer.removePlaceholder(world: World, item: Item): Int {
    val def = item.toUnnoted(world.definitions).getDef(world.definitions)
    val slot = if (def.placeholderLink > 0) indexOfFirst { it?.id == def.placeholderLink && it.amount == 0 } else -1
    if (slot != -1) {
        this[slot] = null
    }
    return slot
}