
import gg.rsmod.plugins.osrs.api.ext.getInteractingOption
import gg.rsmod.plugins.osrs.api.ext.getInteractingSlot
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.api.ext.searchItemDialog
import gg.rsmod.plugins.osrs.content.inter.priceguide.PriceGuide

onButton(parent = 387, child = 19) {
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@onButton
    }
    PriceGuide.open(it.player())
}

onComponentClose(component = PriceGuide.COMPONENT_ID) {
    PriceGuide.close(it.player())
}

onButton(parent = PriceGuide.TAB_COMPONENT_ID, child = 0) {
    it.suspendable {
        PriceGuide.add(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

onButton(parent = PriceGuide.COMPONENT_ID, child = 2) {
    it.suspendable {
        PriceGuide.remove(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

onButton(parent = PriceGuide.COMPONENT_ID, child = 10) {
    PriceGuide.depositInventory(it.player())
}

onButton(parent = PriceGuide.COMPONENT_ID, child = 5) {
    it.suspendable {
        val item = it.searchItemDialog("Select an item to ask about its price:")
        PriceGuide.search(it.player(), item)
    }

}