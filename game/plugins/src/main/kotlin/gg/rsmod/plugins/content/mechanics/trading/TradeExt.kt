package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession

/**
 * An attribute that represents a trade session between two players
 */
val TRADE_SESSION_ATTR = AttributeKey<TradeSession>()

/**
 * An attribute that represents if a player has accepted the trade
 */
val TRADE_ACCEPTED_ATTR = AttributeKey<Boolean>()

/**
 * The attribute holding the set of players who have recently requested a trade
 * with the player
 */
val TRADE_REQUESTS = AttributeKey<HashSet<Player>>()

/**
 * If the [Player] has a [TradeSession]
 */
fun Player.hasTradeSession() = this.attr.has(TRADE_SESSION_ATTR)

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

/**
 * Gets the set of trade requests for a [Player]
 */
fun Player.getTradeRequests() : HashSet<Player> = attr[TRADE_REQUESTS]!!