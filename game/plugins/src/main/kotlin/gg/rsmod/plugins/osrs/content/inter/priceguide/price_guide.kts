
import gg.rsmod.plugins.osrs.api.helper.getInteractingOption
import gg.rsmod.plugins.osrs.api.helper.getInteractingSlot
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.searchItemDialog
import gg.rsmod.plugins.osrs.content.inter.priceguide.PriceGuide

r.bindButton(parent = 387, child = 19) {
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@bindButton
    }
    PriceGuide.open(it.player())
}

r.bindInterfaceClose(parent = PriceGuide.INTERFACE_ID) {
    PriceGuide.close(it.player())
}

r.bindButton(parent = PriceGuide.TAB_INTERFACE_ID, child = 0) {
    it.suspendable {
        PriceGuide.add(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

r.bindButton(parent = PriceGuide.INTERFACE_ID, child = 2) {
    it.suspendable {
        PriceGuide.remove(it, it.getInteractingSlot(), it.getInteractingOption())
    }
}

r.bindButton(parent = PriceGuide.INTERFACE_ID, child = 10) {
    PriceGuide.depositInventory(it.player())
}

r.bindButton(parent = PriceGuide.INTERFACE_ID, child = 5) {
    it.suspendable {
        val item = it.searchItemDialog("Select an item to ask about its price:")
        PriceGuide.search(it.player(), item)
    }

}