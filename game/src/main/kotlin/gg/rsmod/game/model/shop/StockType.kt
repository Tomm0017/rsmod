package gg.rsmod.game.model.shop

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class StockType {
    /**
     * The amount of items in the shop never decrease.
     */
    INFINITE,

    /**
     * The amount of items in the shop is shared across all players.
     */
    GLOBAL,

    /**
     * The amount of items in the shop will only decrement or increment
     * for each individual player depending on their individual transactions.
     */
    INDIVIDUAL
}