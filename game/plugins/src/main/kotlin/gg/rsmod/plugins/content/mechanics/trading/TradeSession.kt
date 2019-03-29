package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
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
     * Opens the trade session, and configures the interfaces
     */
    fun open() {

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
        player.sendItemContainer(24, container)

        partner.sendItemContainer(23, container)
    }

    /**
     * Initialises the trade containers and enables the item container components for the player
     */
    private fun initTradeContainers() {
        player.runClientScript(INTERFACE_INV_INIT_BIG, PLAYER_TRADE_HASH, 24, 4, 7, 0, -1, "Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X", "Examine")
        player.setInterfaceEvents(interfaceId = TRADE_INTERFACE, component = PLAYER_TRADE_HASH, range = 0..container.capacity, setting = 1086)

        player.runClientScript(INTERFACE_INV_INIT_BIG, PARTNER_TRADE_HASH, 23, 4, 7, 0, -1, "Examine")
        player.setInterfaceEvents(interfaceId = TRADE_INTERFACE, component = PARTNER_TRADE_HASH, range = 0..container.capacity, setting = 1086)
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
        val item = inventory[slot]?: return

        inventory.remove(item)
        container.add(item)

        refresh()
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
         * The accept trade interface
         */
        val ACCEPT_INTERFACE = 334

        /**
         * An attribute that represents a trade session between two players
         */
        val TRADE_SESSION_ATTR = AttributeKey<WeakReference<TradeSession>>()

        /**
         * The message that is shown to the partner when a player declined a trade
         */
        val TRADE_DECLINED_MESSAGE = "<col=FF0000>Other player has declined the trade!</col>"
    }
}

fun Int.getInterfaceHash() = (this shl 16)

fun Player.getTradeSession() : TradeSession? = this.attr[TradeSession.TRADE_SESSION_ATTR]?.get()

private fun Player.removeTradeSession() { this.attr[TradeSession.TRADE_SESSION_ATTR]?.clear() }