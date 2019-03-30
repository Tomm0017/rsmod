package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.TRADE_ACCEPTED_ATTR
import gg.rsmod.plugins.service.marketvalue.ItemMarketValueService
import java.lang.ref.WeakReference

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
     * Opens the trade session, and configures the interfaces
     */
    fun open() {

        // Ensure the player isn't still marked as having accepted the trade
        player.attr[TRADE_ACCEPTED_ATTR] = false

        // Configure the trade text
        player.setComponentText(TRADE_INTERFACE, 31, "Trading with: ${partner.username}")

        // Open the trade screen interfaces
        player.openInterface(TRADE_INTERFACE, InterfaceDestination.MAIN_SCREEN)
        player.openInventoryOverlay(OVERLAY_INTERFACE, player.inventory, "Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X")

        // Initialise the trade containers
        initTradeContainers()
    }

    /**
     * Refreshes the item containers for both players
     */
    private fun refresh() {
        player.sendInventoryOverlay(inventory)
        player.sendItemContainer(PLAYER_CONTAINER_KEY, container)

        // Send this player's container data to their partner
        partner.sendItemContainer(PARTNER_CONTAINER_KEY, container)
        partner.setComponentText(TRADE_INTERFACE, 9, "${player.username} has ${inventory.freeSlotCount} free inventory slots.")

        // The value of the container
        val value = container.sumBy { (priceService?.get(it?.id!!) ?: it?.getDef(player.world.definitions)?.cost ?: 0) * (it?.amount ?: 1) }.format()

        // Calculate the trade value
        player.setComponentText(TRADE_INTERFACE, 24, "Your offer:<br>(Value: <col=FFFFFF>$value</col> coins)")
        partner.setComponentText(TRADE_INTERFACE, 27, "${player.username} offers:<br>(Value: <col=FFFFFF>$value</col> coins)")
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

    fun offer(slot: Int, amount: Int) {
        if (stage != TradeStage.TRADE_SCREEN) return

        val item = inventory[slot]?: return
        val count = Math.min(amount, inventory.getItemCount(item.id))

        inventory.remove(item.id, count)
        container.add(item.id, count)

        refresh()

        progress(false)
    }

    fun remove(slot: Int, amount: Int) {
        if (stage != TradeStage.TRADE_SCREEN) return

        val item = container[slot] ?: return
        val count = Math.min(amount, container.getItemCount(item.id))

        container.remove(item.id, count)
        inventory.add(item.id, count)

        container.shift()
        refresh()

        progress(false)
    }

    fun progress(accepted: Boolean = true) {
        player.attr[TRADE_ACCEPTED_ATTR] = accepted

        // If the current trade session is on the trade screen
        if (stage == TradeStage.TRADE_SCREEN) {

            // If the player revoked their acception of the trade offer
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

            // If the player revoked their acception of the trade offer
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

        // The value of the container
        val value = container.sumBy { (priceService?.get(it?.id!!) ?: it?.getDef(player.world.definitions)?.cost ?: 0) * (it?.amount ?: 1) }.format()

        // Send the default component text values
        player.setComponentText(ACCEPT_INTERFACE, 4, "Are you sure you want to make this trade?")
        player.setComponentText(ACCEPT_INTERFACE, 30, "Trading with:<br>${partner.username}")
        player.setComponentText(ACCEPT_INTERFACE, 23, "You are about to give:<br>(Value: <col=FFFFFF>$value</col> coins)")
        partner.setComponentText(ACCEPT_INTERFACE, 24, "In return you will receive:<br>(Value: <col=FFFFFF>$value</col> coins)")

        // Send the item containers
        player.sendItemContainer(90, container)
        partner.getTradeSession()?.let { player.sendItemContainer(-71000, 0, 90, it.container) }

        // Open the accept screen interface
        player.openInterface(ACCEPT_INTERFACE, InterfaceDestination.MAIN_SCREEN)

        // Reset the accept state
        player.attr[TRADE_ACCEPTED_ATTR] = false
    }

    private fun displayOffer(child: Int, container: ItemContainer) {
        val split = container.occupiedSlotCount > 14

       // player.setComponentHidden(ACCEPT_INTERFACE, child, false)
       // player.setComponentText(ACCEPT_INTERFACE, child, getDisplayMessage(container.filterNotNull()))
    }

    private fun getDisplayMessage(items: List<Item>) : String {
        val bldr = StringBuilder()

        when (items.size) {
            0 -> bldr.append("Absolutely nothing!")
            else -> {
                items.forEach {
                    val def = it.getDef(player.world.definitions)
                    bldr.append("<col=FF9040>${def.name}${if (it.amount > 1) "<col=FFFFFF> x ${it.amount.format()}<br>" else "<br>"}")
                }
            }
        }

        return bldr.toString()
    }

    private fun complete() {

        // Assign the trade containers for this player
        val playerInv = player.inventory
        playerInv.removeAll()
        inventory.filterNotNull().forEach { playerInv.add(it) }
        partner.getTradeSession()?.container?.filterNotNull()?.forEach { playerInv.add(it) }

        // Assign the trade containers for the partner
        val partnerInv = partner.inventory
        partnerInv.removeAll()
        partner.getTradeSession()?.inventory?.filterNotNull()?.forEach { partnerInv.add(it) }
        container.filterNotNull().forEach { partnerInv.add(it) }

        // Remove the trade session
        player.removeTradeSession()
        partner.removeTradeSession()

        // Close the trade interface
        player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        player.closeInterface(OVERLAY_INTERFACE)
        partner.closeInterface(InterfaceDestination.MAIN_SCREEN)
        partner.closeInterface(OVERLAY_INTERFACE)
    }

    companion object {

        /**
         * The inventory overlay interface
         */
        val OVERLAY_INTERFACE = 336

        /**
         * The primary trade screen interface
         */
        val TRADE_INTERFACE = 335

        /**
         * The child id of this player's trade offer
         */
        val PLAYER_TRADE_CHILD = 25

        /**
         * The child id of the partner's trade offer
         */
        val PARTNER_TRADE_CHILD = 28

        /**
         * The hash of this player's trade offer component
         */
        val PLAYER_TRADE_HASH = TRADE_INTERFACE.getInterfaceHash() or PLAYER_TRADE_CHILD

        /**
         * The hash of the partner's trade offer component
         */
        val PARTNER_TRADE_HASH = TRADE_INTERFACE.getInterfaceHash() or PARTNER_TRADE_CHILD

        /**
         * The progress trade interface
         */
        val ACCEPT_INTERFACE = 334

        /**
         * An attribute that represents a trade session between two players
         */
        val TRADE_SESSION_ATTR = AttributeKey<TradeSession?>()

        /**
         * An attribute that represents if a player has accepted the trade
         */
        val TRADE_ACCEPTED_ATTR = AttributeKey<Boolean>()

        /**
         * The message that is shown to the partner when a player declined a trade
         */
        val TRADE_DECLINED_MESSAGE = "Other player declined trade."

        /**
         * The container key for this player's trade offer
         */
        val PLAYER_CONTAINER_KEY = 24

        /**
         * The container key for the partner's trade offer
         */
        val PARTNER_CONTAINER_KEY = 23
    }
}

fun Int.getInterfaceHash() = (this shl 16)

fun Player.getTradeSession() : TradeSession? = this.attr[TradeSession.TRADE_SESSION_ATTR]

fun Player.hasAcceptedTrade() : Boolean = this.attr[TRADE_ACCEPTED_ATTR] ?: false

private fun Player.removeTradeSession() {
    this.attr[TradeSession.TRADE_SESSION_ATTR] = null
    this.attr[TRADE_ACCEPTED_ATTR] = false
}