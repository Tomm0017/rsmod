package gg.rsmod.plugins.content.inter.options

import gg.rsmod.plugins.content.inter.options.OptionsTab.OPTIONS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.options.OptionsTab.HOUSE_OPT_BUTTON_ID
import gg.rsmod.plugins.content.inter.options.OptionsTab.HOUSE_OPT_INTERFACE_ID

on_button(interfaceId = OPTIONS_INTERFACE_ID, component = HOUSE_OPT_BUTTON_ID) {
    /**
     Teleport inside + doors: varp 1047
     */
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    player.openInterface(interfaceId = HOUSE_OPT_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
    player.setComponentText(interfaceId = HOUSE_OPT_INTERFACE_ID, component = 20, text = "Number of rooms: 9")
}

/**
 * Close.
 */
on_button(interfaceId = HOUSE_OPT_INTERFACE_ID, component = 21) {
    player.closeInterface(interfaceId = HOUSE_OPT_INTERFACE_ID)
}