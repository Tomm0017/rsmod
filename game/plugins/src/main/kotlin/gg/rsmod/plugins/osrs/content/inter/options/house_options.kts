import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.helper.closeInterface
import gg.rsmod.plugins.osrs.api.helper.openInterface
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setComponentText
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

onButton(parent = OptionsTab.COMPONENT_ID, child = 98) {
    /**
     Teleport inside + doors: varp 1047
     */
    val p = it.player()
    if (!p.lock.canComponentInteract()) {
        return@onButton
    }
    p.openInterface(component = 370, pane = InterfaceDestination.TAB_AREA)
    p.setComponentText(parent = 370, child = 20, text = "Number of rooms: 9")
}

onButton(parent = 370, child = 21) {
    it.player().closeInterface(interfaceId = 370)
}