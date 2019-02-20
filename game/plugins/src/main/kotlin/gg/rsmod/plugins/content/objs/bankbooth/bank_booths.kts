package gg.rsmod.plugins.content.objs.bankbooth

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.openInterface
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setInterfaceUnderlay
import gg.rsmod.plugins.content.inter.bank.openBank

val BOOTHS = arrayOf(
        Objs.BANK_BOOTH, Objs.BANK_BOOTH_6943, Objs.BANK_BOOTH_24101
)

BOOTHS.forEach { booth ->
    on_obj_option(obj = booth, option = "bank") {
        it.player().openBank()
    }

    on_obj_option(obj = booth, option = "collect") {
        open_collect(it.player())
    }
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}