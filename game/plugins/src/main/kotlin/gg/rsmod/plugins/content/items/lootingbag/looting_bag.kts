package gg.rsmod.plugins.content.items.lootingbag

import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.entity.Player
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
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Value: ")

    set_interrupt_queue(p)
}

fun deposit(p: Player) {
    p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
    p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 542)
    p.runClientScript(495, "Add to bag", 1)
    p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = 6, text = "Value: ")

    set_interrupt_queue(p)
}

fun set_interrupt_queue(p: Player) {
    p.queue {
        onInterrupt = {
            if (player.interfaces.isVisible(TAB_INTERFACE_ID)) {
                player.closeInterface(TAB_INTERFACE_ID)
            }
        }
        waitInterfaceClose(TAB_INTERFACE_ID)
    }
}