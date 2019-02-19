package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.model.shop.Shop

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Shop.getSellPrice(world: World, item: Int): Int = world.definitions.get(ItemDef::class.java, item).cost

fun Shop.getBuyPrice(world: World, item: Int): Int = (world.definitions.get(ItemDef::class.java, item).cost * 0.6).toInt()