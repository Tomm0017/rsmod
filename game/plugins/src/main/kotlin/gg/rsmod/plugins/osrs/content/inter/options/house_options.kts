import gg.rsmod.plugins.osrs.api.InterfacePane
import gg.rsmod.plugins.osrs.api.helper.closeInterface
import gg.rsmod.plugins.osrs.api.helper.openInterface
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setInterfaceText
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

r.bindButton(parent = OptionsTab.INTERFACE_ID, child = 98) {
    /**
     Teleport inside + doors: varp 1047
     */
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@bindButton
    }
    p.openInterface(interfaceId = 370, pane = InterfacePane.TAB_AREA)
    p.setInterfaceText(parent = 370, child = 20, text = "Number of rooms: 9")
}

r.bindButton(parent = 370, child = 21) {
    it.player().closeInterface(interfaceId = 370)
}