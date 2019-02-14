package gg.rsmod.game.message.handler

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeld2Message
import gg.rsmod.game.model.INTERACTING_ITEM
import gg.rsmod.game.model.INTERACTING_ITEM_ID
import gg.rsmod.game.model.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.entity.Client
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeld2Handler : MessageHandler<OpHeld2Message> {

    override fun handle(client: Client, message: OpHeld2Message) {
        @Suppress("unused")
        val interfaceId = message.componentHash shr 16
        @Suppress("unused")
        val component = message.componentHash and 0xFFFF

        if (message.slot < 0 || message.slot >= client.inventory.capacity) {
            return
        }

        if (!client.lock.canItemInteract()) {
            return
        }

        val item = client.inventory[message.slot] ?: return

        if (item.id != message.item) {
            logVerificationFail(client, "Item action 2: id=%d, slot=%d, component=(%d, %d), inventory=(%d, %d)",
                    message.item, message.slot, interfaceId, component, item.id, item.amount)
            return
        }

        log(client, "Item action 2: id=%d, slot=%d, component=(%d, %d), inventory=(%d, %d)",
                message.item, message.slot, interfaceId, component, item.id, item.amount)

        client.attr[INTERACTING_ITEM_SLOT] = message.slot
        client.attr[INTERACTING_ITEM_ID] = item.id
        client.attr[INTERACTING_ITEM] = WeakReference(item)

        val result = EquipAction.equip(client, item, message.slot)
        if (result == EquipAction.Result.UNHANDLED) {
            client.message("Unhandled item action: [item=${item.id}, slot=${message.slot}, option=2]")
        }
    }
}