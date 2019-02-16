package gg.rsmod.plugins.osrs.content.inter.xpdrops

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.InterfaceDestination
import gg.rsmod.plugins.osrs.api.OSRSGameframe
import gg.rsmod.plugins.osrs.api.ext.*

val INTERFACE_ID = 122
val SETUP_INTERFACE_ID = 137

val SELECTED_SKILL_VARBIT = 4703

on_button(parent = 160, child = 1) {
    val player = it.player()
    val option = it.getInteractingOption()

    if (option == 1) {
        player.toggleVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT)

        if (player.getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 1) {
            player.openInterface(INTERFACE_ID, InterfaceDestination.XP_COUNTER)
        } else {
            player.closeInterface(INTERFACE_ID)
        }
    } else if (option == 2 && player.lock.canInterfaceInteract()) {
        open_setup(player)
    }
}

fun open_setup(p: Player) {
    set_selected_skill(p, skill = 0)

    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = SETUP_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)

    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 50, range = 1..3, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 51, range = 1..3, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 52, range = 1..4, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 53, range = 1..32, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 54, range = 1..32, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 55, range = 1..8, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 56, range = 1..2, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 57, range = 1..3, setting = 2)
    p.setInterfaceEvents(interfaceId = SETUP_INTERFACE_ID, component = 16, range = 0..24, setting = 2)
}

fun set_selected_skill(p: Player, skill: Int) {
    p.setVarbit(SELECTED_SKILL_VARBIT, skill)
}