package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.World
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.service.marketvalue.ItemMarketValueService

fun Item.getMarketValue(world: World): Int {
    val service = world.getService(ItemMarketValueService::class.java)
    return service?.get(id) ?: getDef(world.definitions).cost
}