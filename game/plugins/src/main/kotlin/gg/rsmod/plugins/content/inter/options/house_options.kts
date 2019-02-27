package gg.rsmod.plugins.content.inter.options

import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.closeInterface
import gg.rsmod.plugins.api.ext.openInterface
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setComponentText

on_button(interfaceId = OptionsTab.INTERFACE_ID, component = 98) {
    /**
     Teleport inside + doors: varp 1047
     */
    val p = player
    if (!p.lock.canInterfaceInteract()) {
        return@on_button
    }
    p.openInterface(interfaceId = 370, dest = InterfaceDestination.TAB_AREA)
    p.setComponentText(interfaceId = 370, component = 20, text = "Number of rooms: 9")
}

on_button(interfaceId = 370, component = 21) {
    player.closeInterface(interfaceId = 370)
}