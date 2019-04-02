package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession.Companion.ACCEPT_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession.Companion.OVERLAY_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession.Companion.PLAYER_TRADE_CHILD
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession.Companion.TRADE_INTERFACE

/**
 * The number of trade requests
 */
val REQUEST_CAPACITY = 10

/**
 * The message sent when a player requests to trade.
 */
val TRADE_REQ_STRING = "%s wishes to trade with you."

/**
 * Initiate the set of trade requests
 */
on_login { player.attr[TRADE_REQUESTS] = HashSet(REQUEST_CAPACITY) }

/**
 * When a player requests to trade a user, we should first check to see if the player they
 * are interacting with has recently sent them a trade request. If so, we should progress the trade request
 * rather than sending out a new request.
 */
on_player_option(option = "Trade with") {

    // The trade partner instance
    val partner = player.getInteractingPlayer()

    // If the player and partner are the same person
    if (player == partner) return@on_player_option

    // If the player is already in a trade
    if (partner.getTradeSession() != null || partner.isLocked()) {
        player.message("Other player is busy at the moment.")
        return@on_player_option
    }

    // The set of players who have requested the player
    val requests = player.getTradeRequests()

    // If the partner hasn't recently requested a trade
    if (!requests.contains(partner)) {

        // Add the player to the partner's requests
        partner.getTradeRequests().add(player)

        // Send the trade request
        player.message("Sending trade request...")
        partner.message(TRADE_REQ_STRING.format(player.username), ChatMessageType.TRADE_REQ)
    } else {

        // Remove the requests
        player.getTradeRequests().remove(partner)
        partner.getTradeRequests().remove(player)

        // Initiate the trade
        initiate(player, partner)
    }
}

/**
 * Initiates a trade between two players
 *
 * @param player    The first player
 * @param partner   The partner player
 */
fun initiate(player: Player, partner: Player) {

    // The trade session instances
    val playerSession = TradeSession(player, partner)
    val partnerSession = TradeSession(partner, player)

    // Define the session attribute for both players
    player.attr[TRADE_SESSION_ATTR] = playerSession
    partner.attr[TRADE_SESSION_ATTR] = partnerSession

    // Initialise the sessions
    playerSession.open()
    partnerSession.open()
}

// Item Offer Event
on_button(OVERLAY_INTERFACE, 0) {
    player.getTradeSession()?.let { trade ->

        // The player's inventory
        val inventory = player.inventory

        // The item slot, and the option that was pressed
        val slot = player.getInteractingSlot()
        val opt = player.getInteractingOption()

        // The item being traded
        val item = inventory[slot] ?: return@on_button

        // Queue the action, as we might need to access queued dialogue
        player.queue(TaskPriority.WEAK) {

            // The amount being traded
            val amount = when (opt) {
                2 -> 5
                3 -> 10
                4 -> inventory.getItemCount(item.id)
                5 -> inputInt("Enter amount:")
                else -> 1
            }

            // Offer the amount to the trade
            trade.offer(slot, amount)
        }
    }
}

// Item Remove Event
on_button(TRADE_INTERFACE, PLAYER_TRADE_CHILD) {
    player.getTradeSession()?.let { trade ->

        // The player's trade container
        val container = trade.container

        // The item slot, and the option that was pressed
        val slot = player.getInteractingSlot()
        val opt = player.getInteractingOption()

        // The item being traded
        val item = container[slot] ?: return@on_button

        // Queue the action, as we might need to access queued dialogue
        player.queue(TaskPriority.WEAK) {

            // The amount being traded
            val amount = when (opt) {
                2 -> 5
                3 -> 10
                4 -> container.getItemCount(item.id)
                5 -> inputInt("Enter amount:")
                else -> 1
            }

            // Offer the amount to the trade
            trade.remove(slot, amount)
        }
    }
}

// Accept buttons
on_button(TRADE_INTERFACE, 10) { player.getTradeSession()?.progress() }
on_button(ACCEPT_INTERFACE, 13) { player.getTradeSession()?.progress() }

// Decline buttons
on_button(TRADE_INTERFACE, 11) { player.getTradeSession()?.decline() }
on_button(ACCEPT_INTERFACE, 14) { player.getTradeSession()?.decline() }

// Interface close events
on_interface_close(TRADE_INTERFACE)  {

    if (player.hasTradeSession() && !player.hasAcceptedTrade()) {
        player.getTradeSession()?.decline()
    }
}

on_interface_close(ACCEPT_INTERFACE) {

    if (player.hasTradeSession() && !player.hasAcceptedTrade()) {
        player.getTradeSession()?.decline()
    }
}

// Decline the trade when a player logs out
on_logout { player.getTradeSession()?.decline() }