package gg.rsmod.plugins.content.objs.chests

import com.google.common.collect.ImmutableSet

val CHESTS = ImmutableSet.of(Objs.REWARDS_CHEST)

CHESTS.forEach { chest ->
    on_obj_option(obj = chest, option = "search") {
        open(player)
    }
}

fun open(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 206, dest = InterfaceDestination.MAIN_SCREEN)
    p.focusTab(GameframeTab.INVENTORY)
}