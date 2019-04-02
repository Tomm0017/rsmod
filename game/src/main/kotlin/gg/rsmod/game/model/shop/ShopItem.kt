package gg.rsmod.game.model.shop

/**
 * Represents an item in a shop.
 *
 * @param item
 * The item id.
 *
 * @param amount
 * The amount of the item that will initially be available for the shop.
 *
 * @param sellPrice
 * The price at which this item is sold on the store.
 *
 * @param buyPrice
 * The price at which this item will be bought back from players, if applicable.
 *
 * @param resupplyAmount
 * The amount of the item that will be resupplied on the "resupply tick".
 *
 * @param resupplyCycles
 * The amount of cycles for each "resupply tick".
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class ShopItem(val item: Int, val amount: Int, val sellPrice: Int? = null, val buyPrice: Int? = null,
                    val resupplyAmount: Int = Shop.DEFAULT_RESUPPLY_AMOUNT,
                    val resupplyCycles: Int = Shop.DEFAULT_RESUPPLY_CYCLES) {

    /**
     * The current amount of the item that is in stock.
     */
    var currentAmount: Int = amount
}