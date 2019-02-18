package gg.rsmod.plugins.content.inter.priceguide

import gg.rsmod.plugins.api.ext.getInteractingOption
import gg.rsmod.plugins.api.ext.getInteractingSlot
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.searchItemInput

on_button(interfaceId = 387, component = 19) {
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@on_button
    }
    PriceGuide.open(it.player())
}

on_interface_close(interfaceId = PriceGuide.COMPONENT_ID) {
    PriceGuide.close(it.player())
}

on_button(interfaceId = PriceGuide.TAB_COMPONENT_ID, component = 0) {
    it.suspendable {
        PriceGuide.add(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

on_button(interfaceId = PriceGuide.COMPONENT_ID, component = 2) {
    it.suspendable {
        PriceGuide.remove(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

on_button(interfaceId = PriceGuide.COMPONENT_ID, component = 10) {
    PriceGuide.depositInventory(it.player())
}

on_button(interfaceId = PriceGuide.COMPONENT_ID, component = 5) {
    it.suspendable {
        val item = it.searchItemInput("Select an item to ask about its price:")
        PriceGuide.search(it.player(), item)
    }

}