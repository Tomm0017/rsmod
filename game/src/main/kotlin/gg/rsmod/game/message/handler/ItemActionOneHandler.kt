package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ItemActionOneMessage
import gg.rsmod.game.model.INTERACTING_ITEM
import gg.rsmod.game.model.INTERACTING_ITEM_ID
import gg.rsmod.game.model.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemActionOneHandler : MessageHandler<ItemActionOneMessage> {

    override fun handle(client: Client, message: ItemActionOneMessage) {
        @Suppress("unused")
        val interfaceParent = message.interfaceHash shr 16
        @Suppress("unused")
        val interfaceChild = message.interfaceHash and 0xFFFF

        println("parent=$interfaceParent, child=$interfaceChild, item=${message.item}, slot=${message.slot}")

        if (message.slot < 0 || message.slot >= client.inventory.capacity) {
            return
        }

        if (client.isDead() || !client.lock.canEat()) {
            return
        }

        val item = client.inventory[message.slot]

        if (item == null || item.id != message.item) {
            logAntiCheat(client, "Item action 1: id=%d, slot=%d, interface=(%d, %d), inventory=(%d, %d)")
            return
        }

        log(client, "Item action 1: id=%d, slot=%d, interface=(%d, %d), inventory=(%d, %d)")

        client.attr[INTERACTING_ITEM_SLOT] = message.slot
        client.attr[INTERACTING_ITEM_ID] = item.id
        client.attr[INTERACTING_ITEM] = item

        client.world.plugins.executeItem(client, item.id, 1)
    }

}