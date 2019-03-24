package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldDMessage
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldDHandler : MessageHandler<OpHeldDMessage> {

    override fun handle(client: Client, message: OpHeldDMessage) {
        val interfaceId = message.componentHash shr 16
        val component = message.componentHash and 0xFFFF
        val srcSlot = message.srcSlot
        val dstSlot = message.dstSlot

        log(client, "Swap component item: interfaceId=%d, component=%d, srcSlot=%d, dstSlot=%d", interfaceId, component, srcSlot, dstSlot)

        client.attr[INTERACTING_ITEM_SLOT] = srcSlot
        client.attr[OTHER_ITEM_SLOT_ATTR] = dstSlot

        val swapped = client.world.plugins.executeComponentItemSwap(client, interfaceId, component)
        if (!swapped && client.world.devContext.debugButtons) {
            client.message("Unhandled component swap: [interface=$interfaceId, component=$component, fromSlot=$srcSlot, toSlot=$dstSlot]")
        }
    }
}