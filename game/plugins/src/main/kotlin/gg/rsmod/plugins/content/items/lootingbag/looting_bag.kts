package gg.rsmod.plugins.content.items.lootingbag

import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.attr.GROUNDITEM_PICKUP_TRANSACTION
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.queue.TaskPriority

val CONTAINER_KEY = ContainerKey("looting_bag", capacity = 28, stackType = ContainerStackType.NORMAL)
val CONTAINER_ID = 516
val TAB_INTERFACE_ID = 81

register_container_key(CONTAINER_KEY) // Mark key as needing to be de-serialized on log-in.

on_login {
    /**
     * If a player has a looting bag when they log in, we need to send the item
     * container. If you open a bank before checking/depositing an item
     * in your looting bag, the bag won't have the "view" option on it.
     */
    if (player.inventory.containsAny(Items.LOOTING_BAG, Items.LOOTING_BAG_22586)) {
        val container = player.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(player.world.definitions, CONTAINER_KEY) }
        player.sendItemContainer(CONTAINER_ID, container)
    }
}

on_global_item_pickup {
    if (player.inventory.contains(Items.LOOTING_BAG_22586) && in_wilderness(player)) {
        val inventoryTransaction = player.attr[GROUNDITEM_PICKUP_TRANSACTION]?.get() ?: return@on_global_item_pickup
        val transactionItem = inventoryTransaction.items.first()
        store(player, transactionItem.item, transactionItem.item.amount, transactionItem.slot)
    }
}

on_item_option(Items.LOOTING_BAG, "open") {
    open(player, getInteractingItemSlot())
    player.message("You open your looting bag, ready to fill it.")
}

on_item_option(Items.LOOTING_BAG_22586, "close") {
    close(player, getInteractingItemSlot())
    player.message("You close your looting bag.")
}

arrayOf(Items.LOOTING_BAG, Items.LOOTING_BAG_22586).forEach { bag ->
    on_item_option(bag, "check") {
        check(player)
    }

    on_item_option(bag, "deposit") {
        deposit(player)
    }
}

on_button(interfaceId = TAB_INTERFACE_ID, component = 5) {
    val slot = getInteractingSlot()
    when (getInteractingOption()) {
        1 -> store(player, slot = slot, amount = 1)
        2 -> store(player, slot = slot, amount = 5)
        3 -> store(player, slot = slot, amount = Int.MAX_VALUE)
        4 -> player.queue { store(player, slot = slot, amount = inputInteger()) }
        5 -> {
            val container = player.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(player.world.definitions, CONTAINER_KEY) }
            val item = container[slot] ?: return@on_button
            player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        }
        9 -> {
            val item = player.inventory[slot] ?: return@on_button
            player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        }
    }
}

/**
 * "Bank your loot"
 * Bank items from your looting bag.
 */
on_button(interfaceId = 15, component = 10) {
    val slot = getInteractingSlot()
    when (getInteractingOption()) {
        1 -> bank(player, slot = slot, amount = 1)
        2 -> bank(player, slot = slot, amount = 5)
        3 -> bank(player, slot = slot, amount = Int.MAX_VALUE)
        4 -> player.queue { bank(player, slot = slot, amount = inputInteger()) }
        10 -> {
            val item = player.containers[CONTAINER_KEY]?.get(slot) ?: return@on_button
            player.world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        }
    }
}

on_button(TAB_INTERFACE_ID, component = 2) {
    player.closeInterface(TAB_INTERFACE_ID)
}

// TODO:
fun in_wilderness(p: Player): Boolean = true

fun store(p: Player, slot: Int, amount: Int) {
    val item = p.inventory[slot] ?: return

    if (item.id == Items.LOOTING_BAG || item.id == Items.LOOTING_BAG_22586) {
        p.message("You may be surprised to learn that bagception is not permitted.")
        return
    }

    // TODO: coins are set to untradeable for some reason
    if (!item.toUnnoted(p.world.definitions).getDef(p.world.definitions).tradeable) {
        p.message("Only tradeable items can be put in the bag.")
        return
    }

    if (!in_wilderness(p)) {
        p.message("You can't put items in the looting bag unless you're in the Wilderness.")
        return
    }

    store(p, item, amount)
}

fun store(p: Player, item: Item, amount: Int, beginSlot: Int = -1): Boolean {
    val container = p.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(p.world.definitions, CONTAINER_KEY) }

    val transferred = p.inventory.transfer(container, item = Item(item, amount).copyAttr(item), beginSlot = beginSlot)
    if (transferred == 0) {
        p.message("The bag's too full.")
        return false
    }
    p.sendItemContainer(CONTAINER_ID, container)
    return true
}

fun bank(p: Player, slot: Int, amount: Int) {
    val container = p.containers[CONTAINER_KEY] ?: return
    val item = container[slot] ?: return

    val transfer = container.transfer(p.bank, item = Item(item, amount).copyAttr(item), unnote = true)
    if (transfer == 0) {
        p.message("Bank full.")
        return
    }
    p.sendItemContainer(CONTAINER_ID, container)
}

fun open(p: Player, slot: Int) {
    val remove = p.inventory.remove(item = Items.LOOTING_BAG, beginSlot = slot)
    if (remove.hasSucceeded()) {
        p.inventory.add(item = Items.LOOTING_BAG_22586, beginSlot = slot)
    }
}

fun close(p: Player, slot: Int) {
    val remove = p.inventory.remove(item = Items.LOOTING_BAG_22586, beginSlot = slot)
    if (remove.hasSucceeded()) {
        p.inventory.add(item = Items.LOOTING_BAG, beginSlot = slot)
    }
}

fun check(p: Player) {
    val container = p.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(p.world.definitions, CONTAINER_KEY) }

    p.runClientScript(149, 81 shl 16 or 5, 516, 4, 7, 0, -1, "", "", "", "", "Examine")
    p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
    p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 32)

    p.runClientScript(495, "Looting bag", 0)
    p.sendItemContainer(CONTAINER_ID, container)
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Value: ${container.getNetworth(p.world).decimalFormat()} coins")

    set_queue(p)
}

fun deposit(p: Player) {
    p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
    p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 542)
    p.runClientScript(495, "Add to bag", 1)
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Bag value: ${p.inventory.getNetworth(p.world).decimalFormat()} coins")

    set_queue(p)
}

fun set_queue(p: Player) {
    p.queue(TaskPriority.STRONG) {
        onInterrupt = {
            if (p.interfaces.isVisible(TAB_INTERFACE_ID)) {
                p.closeInterface(TAB_INTERFACE_ID)
            }
        }
        waitInterfaceClose(TAB_INTERFACE_ID)
    }
}