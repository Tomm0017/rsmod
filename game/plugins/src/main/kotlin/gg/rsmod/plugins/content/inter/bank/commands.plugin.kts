package gg.rsmod.plugins.content.inter.bank

import gg.rsmod.game.model.priv.Privilege

on_command("obank", Privilege.ADMIN_POWER) {
    player.openBank()
}

/**
 * Clears all bank tab varbits for the player, effectively
 * dumping all items back into the one main tab.
 */
on_command("tabreset"){
    for(tab in 1..9)
        player.setVarbit(BankTabs.BANK_TAB_ROOT_VARBIT +tab, 0)
    player.setVarbit(BankTabs.SELECTED_TAB_VARBIT, 0)
}