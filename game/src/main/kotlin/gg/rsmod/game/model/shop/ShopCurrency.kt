package gg.rsmod.game.model.shop

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Player

/**
 * TODO: redo the whole concept of [ShopCurrency].
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface ShopCurrency {

    fun sendSellValueMessage(p: Player, item: Int)

    fun sendBuyValueMessage(p: Player, shop: Shop, item: Int)

    fun getSellPrice(world: World, item: Int): Int

    fun getBuyPrice(world: World, item: Int): Int

    fun sellToPlayer(p: Player, shop: Shop, slot: Int, amt: Int)

    fun buyFromPlayer(p: Player, shop: Shop, slot: Int, amt: Int)
}