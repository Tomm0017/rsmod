package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession

/**
 * Gets the [TradeSession] instance for a player
 */
fun Player.getTradeSession() : TradeSession? = this.attr[TRADE_SESSION_ATTR]

/**
 * If the [Player] has accepted a trade session
 */
fun Player.hasAcceptedTrade() : Boolean = this.attr[TRADE_ACCEPTED_ATTR] ?: false

/**
 * Removes the [TradeSession] instance from a [Player]
 */
fun Player.removeTradeSession() {
    this.attr.remove(TRADE_SESSION_ATTR)
    this.attr.remove(TRADE_ACCEPTED_ATTR)
}