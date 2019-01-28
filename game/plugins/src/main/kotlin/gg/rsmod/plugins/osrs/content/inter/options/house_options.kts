import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.helper.closeComponent
import gg.rsmod.plugins.osrs.api.helper.openComponent
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
    p.openComponent(component = 370, pane = ComponentPane.TAB_AREA)
    p.setComponentText(parent = 370, child = 20, text = "Number of rooms: 9")
}

onButton(parent = 370, child = 21) {
    it.player().closeComponent(component = 370)
}