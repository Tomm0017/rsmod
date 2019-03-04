package gg.rsmod.plugins.content.items.lootingbag

import gg.rsmod.game.model.container.ContainerKey
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

val CONTAINER_KEY = ContainerKey("looting_bag")
val CONTAINER_ID = 516
val INVENTORY_INTERFACE_ID = 81

on_item_option(Items.LOOTING_BAG, "open") {
    open(player)
}

fun open(p: Player) {
    if (!p.containers.containsKey(CONTAINER_KEY)) {
        p.containers[CONTAINER_KEY] = ItemContainer(p.world.definitions, capacity = 28, stackType = ContainerStackType.NORMAL)
    }
    val container = p.containers[CONTAINER_KEY]!!

    p.runClientScript(149, 81 shl 16 or 5, 516, 4, 7, 0, -1, "", "", "", "", "Examine")
    p.openInterface(dest = InterfaceDestination.INVENTORY, interfaceId = INVENTORY_INTERFACE_ID)
    p.setInterfaceEvents(interfaceId = INVENTORY_INTERFACE_ID, component = 5, range = 0..27, setting = 32)

    p.runClientScript(495, "Looting bag", 0)
    p.sendItemContainer(CONTAINER_ID, container)
    p.setComponentText(interfaceId = INVENTORY_INTERFACE_ID, component = 6, text = "Value: -")
}