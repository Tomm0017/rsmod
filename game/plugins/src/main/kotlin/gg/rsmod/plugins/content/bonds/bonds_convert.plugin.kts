package gg.rsmod.plugins.content.bonds

import gg.rsmod.plugins.content.bonds.BondPackages.TRADEABLE_BOND
import gg.rsmod.plugins.content.bonds.BondPackages.UNTRADEABLE_BOND
import gg.rsmod.plugins.content.inter.options.OptionsTab.BONDS_INTERFACE_ID

/**
 * Decoupled the convert bond interface for improved readability
 * of main plugin `bond_pouch` as well as abstraction and purposed
 * use of the convert bond interface
 */
on_button(interfaceId = BONDS_INTERFACE_ID, component = 75) {
    player.playSound(2266, 1, 0)
    player.runClientScript(2524, -1, -1)
    player.openInterface(interfaceId = 64, dest = InterfaceDestination.MAIN_SCREEN)
    val cost = (Item(TRADEABLE_BOND).getMarketValue(player.world) * 0.1).toInt()

    val amount = Math.min(player.bonds.getItemCount(UNTRADEABLE_BOND), player.inventory.getItemCount(Items.COINS_995)/cost)
    player.runClientScript(723, amount, cost)
}