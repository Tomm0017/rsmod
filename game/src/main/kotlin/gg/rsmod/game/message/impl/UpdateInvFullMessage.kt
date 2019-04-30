package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateInvFullMessage(val items: Array<Item?>, val componentHash: Int, val containerKey: Int) : Message {

    constructor(interfaceId: Int, component: Int, containerKey: Int, items: Array<Item?>) : this(items, (interfaceId shl 16) or component, containerKey)

    constructor(interfaceId: Int, component: Int, items: Array<Item?>) : this(items, (interfaceId shl 16) or component, 0)

    constructor(containerKey: Int, items: Array<Item?>) : this(items, -1, containerKey)
}