package gg.rsmod.plugins.content.inter.bank

import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.plugins.content.inter.bank.BankTabs.dropToTab

val BANK_TABLIST_ID = 11

/**
 * Moving items to tabs via the top tabs bar.
 */
on_component_to_component_item_swap(
        srcInterfaceId = Bank.BANK_INTERFACE_ID, srcComponent = Bank.BANK_MAINTAB_COMPONENT,
        dstInterfaceId = Bank.BANK_INTERFACE_ID, dstComponent = BANK_TABLIST_ID) {
    val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!
    dropToTab(player, dstSlot - 10)
}

/**
 * Clears all bank tab varbits for the player, effectively
 * dumping all items back into the one main tab.
 */
on_command("tabreset"){
    for(tab in 1..9)
        player.setVarbit(4170+tab, 0)
}