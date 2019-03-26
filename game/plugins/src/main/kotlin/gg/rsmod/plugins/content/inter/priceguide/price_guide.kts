package gg.rsmod.plugins.content.inter.priceguide

on_button(interfaceId = 387, component = 19) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    PriceGuide.open(player)
}

on_interface_close(interfaceId = PriceGuide.INTERFACE_ID) {
    PriceGuide.close(player)
    player.closeInputDialog()
}

on_button(interfaceId = PriceGuide.TAB_INTERFACE_ID, component = 0) {
    player.queue(TaskPriority.WEAK) {
        PriceGuide.add(this, player.getInteractingSlot(), player.getInteractingOption())
    }
}

on_button(interfaceId = PriceGuide.INTERFACE_ID, component = 2) {
    player.queue(TaskPriority.WEAK) {
        PriceGuide.remove(this, player.getInteractingSlot(), player.getInteractingOption())
    }
}

on_button(interfaceId = PriceGuide.INTERFACE_ID, component = 10) {
    PriceGuide.depositInventory(player)
}

on_button(interfaceId = PriceGuide.INTERFACE_ID, component = 5) {
    player.queue(TaskPriority.WEAK) {
        val item = searchItemInput("Select an item to ask about its price:")
        PriceGuide.search(player, item)
    }
}