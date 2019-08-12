package gg.rsmod.plugins.content.objs.bankchest

import gg.rsmod.plugins.content.inter.bank.openBank

private val CHESTS = setOf(Objs.BANK_CHEST, Objs.BANK_CHEST_14886, Objs.BANK_CHEST_28861)

CHESTS.forEach { chests ->
    on_obj_option(obj = chests, option = "use") {
        player.openBank()
    }

    on_obj_option(obj = chests, option = "collect") {
        open_collect(player)
    }
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}