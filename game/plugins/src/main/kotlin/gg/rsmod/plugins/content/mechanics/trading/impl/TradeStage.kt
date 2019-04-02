package gg.rsmod.plugins.content.mechanics.trading.impl

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the current 'stage' within a trade session
 */
enum class TradeStage {

    /**
     * Represents a trade session that is currently sitting on the first trade
     * screen, where players may offer up items to trade with each other.
     */
    TRADE_SCREEN,

    /**
     * Represents a trade session that is currently sitting on the accept screen, where
     * players may agree to complete the trade.
     */
    ACCEPT_SCREEN,

    /**
     * Represents a trade session that has been completed
     */
    COMPLETED
}