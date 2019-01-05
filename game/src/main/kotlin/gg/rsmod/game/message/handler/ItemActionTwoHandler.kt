package gg.rsmod.game.message.handler

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ItemActionTwoMessage
import gg.rsmod.game.model.INTERACTING_ITEM
import gg.rsmod.game.model.INTERACTING_ITEM_ID
import gg.rsmod.game.model.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemActionTwoHandler : MessageHandler<ItemActionTwoMessage> {

    override fun handle(client: Client, message: ItemActionTwoMessage) {
        @Suppress("unused")
        val interfaceParent = message.interfaceHash shr 16
        @Suppress("unused")
        val interfaceChild = message.interfaceHash and 0xFFFF

        if (message.slot < 0 || message.slot >= client.inventory.capacity) {
            return
        }

        if (client.isDead() || !client.lock.canItemInteract()) {
            return
        }

        val item = client.inventory[message.slot] ?: return

        if (item.id != message.item) {
            logAntiCheat(client, "Item action 2: id=%d, slot=%d, interface=(%d, %d), inventory=(%d, %d)",
                    message.item, message.slot, interfaceParent, interfaceChild, item.id, item.amount)
            return
        }

        log(client, "Item action 2: id=%d, slot=%d, interface=(%d, %d), inventory=(%d, %d)",
                message.item, message.slot, interfaceParent, interfaceChild, item.id, item.amount)

        client.attr[INTERACTING_ITEM_SLOT] = message.slot
        client.attr[INTERACTING_ITEM_ID] = item.id
        client.attr[INTERACTING_ITEM] = item

        EquipAction.equip(client, item, message.slot)
    }

}