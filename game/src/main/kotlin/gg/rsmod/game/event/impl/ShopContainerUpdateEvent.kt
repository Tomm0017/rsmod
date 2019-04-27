package gg.rsmod.game.event.impl

import gg.rsmod.game.event.Event
import gg.rsmod.game.model.item.Item

/**
 * An [Event] that is triggered when a shop refresh action takes place for a
 * [gg.rsmod.game.model.entity.Player].
 *
 * @param items the [Item]s in the shop.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ShopContainerUpdateEvent(val items: Array<Item?>) : Event