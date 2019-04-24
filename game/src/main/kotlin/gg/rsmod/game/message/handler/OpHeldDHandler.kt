package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldDMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldDHandler : MessageHandler<OpHeldDMessage> {

    override fun handle(client: Client, world: World, message: OpHeldDMessage) {
        val interfaceId = message.componentHash shr 16
        val component = message.componentHash and 0xFFFF
        val fromSlot = message.srcSlot
        val toSlot = message.dstSlot

        log(client, "Swap component item: component=[%d, %d], src_slot=%d, dst_slot=%d", interfaceId, component, fromSlot, toSlot)

        client.attr[INTERACTING_ITEM_SLOT] = fromSlot
        client.attr[OTHER_ITEM_SLOT_ATTR] = toSlot

        val swapped = world.plugins.executeComponentItemSwap(client, interfaceId, component)
        if (!swapped && world.devContext.debugButtons) {
            client.message("Unhandled component swap: [component=[$interfaceId, $component], from_slot=$fromSlot, to_slot=$toSlot]")
        }
    }
}