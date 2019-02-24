package gg.rsmod.game.model.shop

/**
 * Represents the supply stock type for a shop.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class StockType {
    /**
     * The amount of items in the shop will decrease globally if/when it's
     * purchased.
     */
    NORMAL,

    /**
     * The amount of items in the shop never decrease.
     */
    INFINITE,
}