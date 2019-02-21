package gg.rsmod.game.model.shop

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ShopItem(val item: Int, val amount: Int, val sellPrice: Int = -1, val buyPrice: Int = -1,
                    val resupplyAmount: Int = Shop.DEFAULT_RESUPPLY_AMOUNT,
                    val resupplyCycles: Int = Shop.DEFAULT_RESUPPLY_CYCLES) {

    var currentAmount: Int = amount
}