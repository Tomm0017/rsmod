package gg.rsmod.plugins.osrs.content.inter.priceguide

import gg.rsmod.plugins.osrs.api.ext.getInteractingOption
import gg.rsmod.plugins.osrs.api.ext.getInteractingSlot
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.searchItemInput

on_button(parent = 387, child = 19) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@on_button
    }
    PriceGuide.open(it.player())
}

on_interface_close(interfaceId = PriceGuide.COMPONENT_ID) {
    PriceGuide.close(it.player())
}

on_button(parent = PriceGuide.TAB_COMPONENT_ID, child = 0) {
    it.suspendable {
        PriceGuide.add(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

on_button(parent = PriceGuide.COMPONENT_ID, child = 2) {
    it.suspendable {
        PriceGuide.remove(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

on_button(parent = PriceGuide.COMPONENT_ID, child = 10) {
    PriceGuide.depositInventory(it.player())
}

on_button(parent = PriceGuide.COMPONENT_ID, child = 5) {
    it.suspendable {
        val item = it.searchItemInput("Select an item to ask about its price:")
        PriceGuide.search(it.player(), item)
    }

}