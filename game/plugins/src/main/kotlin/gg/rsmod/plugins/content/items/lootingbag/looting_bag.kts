package gg.rsmod.plugins.content.items.lootingbag

import gg.rsmod.game.model.ExamineEntityType
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

val CONTAINER_KEY = ContainerKey("looting_bag", capacity = 28, stackType = ContainerStackType.NORMAL)
val CONTAINER_ID = 516
val TAB_INTERFACE_ID = 81

register_container_key(CONTAINER_KEY) // Mark key as needing to be de-serialized on log-in.

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

fun store(p: Player, slot: Int, amount: Int) {
    val item = p.inventory[slot] ?: return

    if (item.id == Items.LOOTING_BAG || item.id == Items.LOOTING_BAG_22586) {
        p.message("You may be surprised to learn that bagception is not permitted.")
        return
    }

    if (!item.toUnnoted(p.world.definitions).getDef(p.world.definitions).isTradeable()) {
        p.message("Only tradeable items can be put in the bag.")
        return
    }

    // TODO: check if in wilderness ("You can't put items in the looting bag unless you're in the Wilderness.")

    p.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(p.world.definitions, CONTAINER_KEY) }
    val container = p.containers[CONTAINER_KEY]!!

    val transferred = p.inventory.transfer(container, item = Item(item, amount).copyAttr(item))
    if (transferred == 0) {
        p.message("The bag's too full.")
        return
    }
    p.sendItemContainer(CONTAINER_ID, container)
}

fun bank(p: Player, slot: Int, amount: Int) {
    val container = p.containers[CONTAINER_KEY] ?: return
    val item = container[slot] ?: return

    container.transfer(p.bank, item = Item(item, amount).copyAttr(item))
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
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Value: ") //##,### coins

    set_queue(p)
}

fun deposit(p: Player) {
    p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
    p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 542)
    p.runClientScript(495, "Add to bag", 1)
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Bag value: ") //##,### coins

    set_queue(p)
}

fun set_queue(p: Player) {
    p.queue(TaskPriority.STRONG) { waitInterfaceClose(TAB_INTERFACE_ID) }
}