package gg.rsmod.game.model.shop

import gg.rsmod.game.model.container.ItemContainer

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Shop(val name: String, val container: ItemContainer, val stockType: StockType)