package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.model.World
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.service.item.ItemValueService

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Item.getMarketValue(world: World): Int {
    val service = world.getService(ItemValueService::class.java).orElse(null)
    return service?.get(id) ?: getDef(world.definitions).cost
}

fun Item.getName(definitions: DefinitionSet): String = getDef(definitions).name

fun Item.getName(world: World): String = getDef(world.definitions).name