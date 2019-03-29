package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.ACCEPT_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.OVERLAY_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.TRADE_INTERFACE
import gg.rsmod.plugins.content.mechanics.trading.TradeSession.Companion.TRADE_SESSION_ATTR
import java.lang.ref.WeakReference

/**
 * The message sent when a player requests to trade.
 */
val TRADE_REQ_STRING = "%s wishes to trade with you."

/**
 * When a player requests to trade a user, we should first check to see if the player they
 * are interacting with has recently sent them a trade request. If so, we should accept the trade request
 * rather than sending out a new request.
 */
on_player_option(option = "Trade with") {

    // The trade partner instance
    val partner = player.getInteractingPlayer()
    partner.message(TRADE_REQ_STRING.format(player.username), ChatMessageType.TRADE_REQ)

    // The trade session instances
    val playerSession = TradeSession(player, partner, world.definitions)
    val partnerSession = TradeSession(partner, player, world.definitions)

    // Define the session attribute for both players
    player.attr[TRADE_SESSION_ATTR] = WeakReference(playerSession)
    partner.attr[TRADE_SESSION_ATTR] = WeakReference(partnerSession)

    // Initialise the sessions
    playerSession.open()
    partnerSession.open()
}

// Item offer events
on_button(OVERLAY_INTERFACE, 0) {
    val trade = player.getTradeSession()
    trade?.let {
        val slot = player.getInteractingSlot()
        val opt = player.getInteractingOption()
    }
}

// Interface close events
on_interface_close(TRADE_INTERFACE)  { player.getTradeSession()?.decline() }
on_interface_close(ACCEPT_INTERFACE) { player.getTradeSession()?.decline() }

// Decline the trade when a player logs out
on_logout { player.getTradeSession()?.decline() }