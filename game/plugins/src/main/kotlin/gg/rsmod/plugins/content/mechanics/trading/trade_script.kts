package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.ACCEPT_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.OVERLAY_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.PLAYER_TRADE_CHILD
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.TRADE_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.TRADE_SESSION_ATTR
import java.lang.ref.WeakReference

/**
 * The message sent when a player requests to trade.
 */
val TRADE_REQ_STRING = "%s wishes to trade with you."

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

    player.message("Sending trade message...")
    partner.message(TRADE_REQ_STRING.format(player.username), ChatMessageType.TRADE_REQ)

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
    val trade = player.getTradeSession()
    trade?.let {

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
            player.getTradeSession()?.offer(slot, amount)
        }
    }
}

// Item Remove Event
on_button(TRADE_INTERFACE, PLAYER_TRADE_CHILD) {
    val trade = player.getTradeSession()
    trade?.let {

        // The player's trade container
        val container = it.container

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
            player.getTradeSession()?.remove(slot, amount)
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
    val trade = player.getTradeSession()

    if (trade != null && !player.hasAcceptedTrade()) {
        player.getTradeSession()?.decline()
    }
}

on_interface_close(ACCEPT_INTERFACE) {
    val trade = player.getTradeSession()

    if (trade != null && !player.hasAcceptedTrade()) {
        player.getTradeSession()?.decline()
    }
}

// Decline the trade when a player logs out
on_logout { player.getTradeSession()?.decline() }