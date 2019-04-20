package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateInvFullMessage constructor(val items: Array<Item?>, val componentHash: Int, val containerKey: Int) : Message {

    constructor(parent: Int, child: Int, containerKey: Int, items: Array<Item?>) : this(items, (parent shl 16) or child, containerKey)

    constructor(parent: Int, child: Int, items: Array<Item?>) : this(items, (parent shl 16) or child, 0)

    constructor(containerKey: Int, items: Array<Item?>) : this(items, -1, containerKey)
}