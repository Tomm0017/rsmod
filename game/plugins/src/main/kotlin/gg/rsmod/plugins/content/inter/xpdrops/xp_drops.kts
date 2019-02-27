package gg.rsmod.plugins.content.inter.xpdrops

import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.OSRSGameframe
import gg.rsmod.plugins.api.ext.*

val INTERFACE_ID = 122
val SETUP_INTERFACE_ID = 137

on_button(interfaceId = 160, component = 1) {
    val player = player
    val option = getInteractingOption()

    if (option == 1) {
        player.toggleVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT)

        if (player.getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
            player.openInterface(INTERFACE_ID, InterfaceDestination.XP_COUNTER)
        } else {
            player.closeInterface(INTERFACE_ID)
        }
    } else if (option == 2 && player.lock.canInterfaceInteract()) {
        // TODO
    }
}