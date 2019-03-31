package gg.rsmod.plugins.content.mechanics.trading.impl

import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.mechanics.trading.*
import gg.rsmod.plugins.service.marketvalue.ItemMarketValueService

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents a trading session between two players
 *
 * @param player    The player this trade session belongs to
 * @param partner   The partner of this trade session
 */
class TradeSession(private val player: Player, private val partner: Player) {

    /**
     * A copy of this player's inventory, so we don't interfere with the player's real inventory unless necessary
     */
    val inventory = ItemContainer(player.inventory)

    /**
     * The trade container for this trade session, in the current player's context
     */
    val container = ItemContainer(player.world.definitions, player.inventory.capacity, ContainerStackType.NORMAL)

    /**
     * The [ItemMarketValueService] instance for this trade session
     */
    private val priceService = player.world.getService(ItemMarketValueService::class.java)

    /**
     * The current 'stage' of the trade session
     */
    private var stage : TradeStage = TradeStage.TRADE_SCREEN

    /**
     * An extension function for retrieving the value of an item container from the [ItemMarketValueService]
     */
    private fun ItemContainer.getValue() = sumBy { (priceService?.get(it?.id!!) ?: it?.getDef(player.world.definitions)?.cost ?: 0) * (it?.amount ?: 1) }.format()

    /**
     * Opens the trade session, and configures the interfaces
     */
    fun open() {

        // Ensure the player isn't still marked as having accepted the trade
        player.attr[TRADE_ACCEPTED_ATTR] = false

        // Configure the trade text
        player.setComponentText(TRADE_INTERFACE, 31, "Trading with: ${partner.username}")

        // Open the inventory overlay
        player.sendItemContainer(key = PLAYER_INVENTORY_KEY, container = inventory)
        player.runClientScript(INTERFACE_INV_INIT_BIG, OVERLAY_INTERFACE.getInterfaceHash(), PLAYER_INVENTORY_KEY, 4, 7, 0, -1, "Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X")
        player.setInterfaceEvents(interfaceId = OVERLAY_INTERFACE, component = 0, range = 0..container.capacity, setting = 1086)
        player.openInterface(OVERLAY_INTERFACE, InterfaceDestination.TAB_AREA)

        // Open the trade screen interface
        player.openInterface(TRADE_INTERFACE, InterfaceDestination.MAIN_SCREEN)

        // Initialise the trade containers
        initTradeContainers()
    }

    /**
     * Refreshes the item containers for both players
     */
    private fun refresh() {
        player.sendItemContainer(key = PLAYER_INVENTORY_KEY, container = inventory)
        player.sendItemContainer(PLAYER_CONTAINER_KEY, container)

        // Send this player's container data to their partner
        partner.sendItemContainer(PARTNER_CONTAINER_KEY, container)
        partner.setComponentText(TRADE_INTERFACE, 9, "${player.username} has ${inventory.freeSlotCount} free inventory slots.")

        // Calculate the trade value
        player.setComponentText(TRADE_INTERFACE, 24, "Your offer:<br>(Value: <col=FFFFFF>${container.getValue()}</col> coins)")
        partner.setComponentText(TRADE_INTERFACE, 27, "${player.username} offers:<br>(Value: <col=FFFFFF>${container.getValue()}</col> coins)")
    }

    /**
     * Initialises the trade containers and enables the item container components for the player
     */
    private fun initTradeContainers() {
        player.runClientScript(INTERFACE_INV_INIT_BIG, PLAYER_TRADE_HASH, PLAYER_CONTAINER_KEY, 4, 7, 0, -1, "Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X", "Examine")
        player.setInterfaceEvents(interfaceId = TRADE_INTERFACE, component = PLAYER_TRADE_HASH, range = 0..container.capacity, setting = 1086)

        player.runClientScript(INTERFACE_INV_INIT_BIG, PARTNER_TRADE_HASH, PARTNER_CONTAINER_KEY, 4, 7, 0, -1, "Examine")
        player.setInterfaceEvents(interfaceId = TRADE_INTERFACE, component = PARTNER_TRADE_HASH, range = 0..container.capacity, setting = 1086)

        refresh()
    }

    /**
     * Declines the trade session for both players
     */
    fun decline() {
        if (partner.getTradeSession() != null) {

            // Remove the trade sessions from both players
            player.removeTradeSession()
            partner.removeTradeSession()

            // Inform the player that they've declined the trade, and close the trade window
            player.message("You declined the trade")
            player.closeInterface(InterfaceDestination.MAIN_SCREEN)
            player.closeInterface(OVERLAY_INTERFACE)

            // Inform the partner that the player has declined the trade, and close their window
            partner.message(TRADE_DECLINED_MESSAGE)
            partner.closeInterface(InterfaceDestination.MAIN_SCREEN)
            partner.closeInterface(OVERLAY_INTERFACE)
        }
    }

    /**
     * Offers an item to this [Player]'s trade [ItemContainer]
     *
     * @param slot      The slot in the temporary inventory
     * @param amount    The amount to offer in trade
     */
    fun offer(slot: Int, amount: Int) {
        if (stage != TradeStage.TRADE_SCREEN) return

        val item = inventory[slot]?: return
        val count = Math.min(amount, inventory.getItemCount(item.id))

        val transaction = inventory.remove(item.id, count, assureFullRemoval = true, beginSlot = slot)
        if (transaction.hasSucceeded())
            container.add(item.id, count)

        refresh()
        progress(false)
    }

    /**
     * Removes an item from this [Player]'s trade [ItemContainer]
     *
     * @param slot      The slot in the trade container
     * @param amount    The amount to remove from the trade container
     */
    fun remove(slot: Int, amount: Int) {
        if (stage != TradeStage.TRADE_SCREEN) return

        val item = container[slot] ?: return
        val count = Math.min(amount, container.getItemCount(item.id))

        val transaction = container.remove(item.id, count, assureFullRemoval = true)
        if (transaction.hasSucceeded()) {
            inventory.add(item.id, count)
            container.shift()
        }

        refresh()
        progress(false)
    }

    /**
     * Progresses this [TradeSession] instance. If both players accept the trade, it will either
     * progress to the accept screen, or complete the trade and give each player the traded items.
     *
     * @param accepted  If the player accepted this trade session
     */
    fun progress(accepted: Boolean = true) {
        player.attr[TRADE_ACCEPTED_ATTR] = accepted

        // If the current trade session is on the trade screen
        if (stage == TradeStage.TRADE_SCREEN) {

            // If the player revoked their acceptation of the trade offer
            if (!player.hasAcceptedTrade()) {

                // Set the partner's option to revoked also
                partner.attr[TRADE_ACCEPTED_ATTR] =  false

                // Reset the component text
                player.setComponentText(TRADE_INTERFACE, 30, "")
                partner.setComponentText(TRADE_INTERFACE, 30, "")
                return
            }

            // If the other player has not accepted, send the confirmation text
            if (player.hasAcceptedTrade() && !partner.hasAcceptedTrade()) {
                player.setComponentText(TRADE_INTERFACE, 30, "Waiting for other player...")
                partner.setComponentText(TRADE_INTERFACE, 30, "Other player has accepted.")
            } else if (player.hasAcceptedTrade() && partner.hasAcceptedTrade()) {

                // Open the accept screen
                openAcceptScreen()
                partner.getTradeSession()?.openAcceptScreen()
            }
        }

        // If the current trade session is on the progress screen
        if (stage == TradeStage.ACCEPT_SCREEN) {

            // If the player revoked their acceptation of the trade offer
            if (!player.hasAcceptedTrade()) {

                // Set the partner's option to revoked also
                partner.attr[TRADE_ACCEPTED_ATTR] =  false

                // Reset the component text
                player.setComponentText(ACCEPT_INTERFACE, 4, "Are you sure you want to make this trade?")
                partner.setComponentText(ACCEPT_INTERFACE, 4, "Are you sure you want to make this trade?")
                return
            }

            // If the other player has not accepted, send the confirmation text
            if (player.hasAcceptedTrade() && !partner.hasAcceptedTrade()) {
                player.setComponentText(ACCEPT_INTERFACE, 4, "Waiting for other player...")
                partner.setComponentText(ACCEPT_INTERFACE, 4, "Other player has accepted.")
            } else if (player.hasAcceptedTrade() && partner.hasAcceptedTrade()) {

                // Complete the trade
                complete()
            }
        }
    }

    /**
     * Opens the accept screen for each player
     */
    private fun openAcceptScreen() {

        // Set the trade stage
        stage = TradeStage.ACCEPT_SCREEN

        // Send the default component text values
        player.setComponentText(ACCEPT_INTERFACE, 4, "Are you sure you want to make this trade?")
        player.setComponentText(ACCEPT_INTERFACE, 30, "Trading with:<br>${partner.username}")
        player.setComponentText(ACCEPT_INTERFACE, 23, "You are about to give:<br>(Value: <col=FFFFFF>${container.getValue()}</col> coins)")
        partner.setComponentText(ACCEPT_INTERFACE, 24, "In return you will receive:<br>(Value: <col=FFFFFF>${container.getValue()}</col> coins)")

        // Send the item containers
        player.sendItemContainer(ACCEPT_CONTAINER_KEY, container)
        partner.getTradeSession()?.let { player.sendItemContainer(-71000, 0, ACCEPT_CONTAINER_KEY, it.container) }

        // Open the accept screen interface
        player.openInterface(ACCEPT_INTERFACE, InterfaceDestination.MAIN_SCREEN)

        // Reset the accept state
        player.attr[TRADE_ACCEPTED_ATTR] = false
    }

    /**
     * Completes the trade session, which swaps the player's trade containers, and
     * sets their inventory to the temporary one operated on during the trade.
     */
    private fun complete() {

        // Assign the trade containers for this player
        val playerInv = player.inventory
        inventory.forEachIndexed { index, item -> playerInv[index] = item }
        partner.getTradeSession()?.container?.filterNotNull()?.forEach { playerInv.add(it) }

        // Assign the trade containers for the partner
        val partnerInv = partner.inventory
        partner.getTradeSession()?.inventory?.forEachIndexed { index, item -> partnerInv[index] = item }
        container.filterNotNull().forEach { partnerInv.add(it) }

        // Finalise the trade session
        finalise(player)
        finalise(partner)
    }

    /**
     * Finalises the trade session by clearing the item containers, removing
     * the session attribute, and closing the trade screen interface
     *
     * @param player    The player to finalise the trade session for
     */
    private fun finalise(player: Player) {

        // Clear the containers
        container.removeAll()
        inventory.removeAll()

        // Remove the trade session
        player.removeTradeSession()

        // Close the trade interface
        player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        player.closeInterface(OVERLAY_INTERFACE)
    }

}