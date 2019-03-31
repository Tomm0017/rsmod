package gg.rsmod.plugins.content.mechanics.trading

import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.ext.getInterfaceHash
import gg.rsmod.plugins.content.mechanics.trading.impl.TradeSession

/**
 * The inventory overlay interface
 */
const val OVERLAY_INTERFACE = 336

/**
 * The primary trade screen interface
 */
const val TRADE_INTERFACE = 335

/**
 * The child id of this player's trade offer
 */
const val PLAYER_TRADE_CHILD = 25

/**
 * The child id of the partner's trade offer
 */
const val PARTNER_TRADE_CHILD = 28

/**
 * The hash of this player's trade offer component
 */
val PLAYER_TRADE_HASH = TRADE_INTERFACE.getInterfaceHash(PLAYER_TRADE_CHILD)

/**
 * The hash of the partner's trade offer component
 */
val PARTNER_TRADE_HASH = TRADE_INTERFACE.getInterfaceHash(PARTNER_TRADE_CHILD)

/**
 * The progress trade interface
 */
const val ACCEPT_INTERFACE = 334

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
const val TRADE_DECLINED_MESSAGE = "Other player declined trade."

/**
 * The message sent when a player requests to trade.
 */
const val TRADE_REQ_STRING = "%s wishes to trade with you."

/**
 * The container key for the trade accept screen
 */
const val ACCEPT_CONTAINER_KEY = 90

/**
 * The container key for the player's inventory overlay
 */
const val PLAYER_INVENTORY_KEY = 93

/**
 * The container key for this player's trade offer
 */
const val PLAYER_CONTAINER_KEY = 24

/**
 * The container key for the partner's trade offer
 */
const val PARTNER_CONTAINER_KEY = 23

/**
 * The attribute holding the set of players who have recently requested a trade
 * with the player
 */
val TRADE_REQUESTS = AttributeKey<HashSet<Player>>()

/**
 * The number of trade requests
 */
const val REQUEST_CAPACITY = 10