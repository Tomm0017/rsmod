package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonDMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButtonDHandler : MessageHandler<IfButtonDMessage> {

    override fun handle(client: Client, world: World, message: IfButtonDMessage) {
        val fromComponentHash = message.srcComponentHash
        val fromSlot = message.srcSlot
        val fromItemId = message.srcItem

        val toComponentHash = message.dstComponentHash
        val toSlot = message.dstSlot
        val toItemId = message.dstItem

        val fromInterfaceId = fromComponentHash shr 16
        val fromComponent = fromComponentHash and 0xFFFF
        val toInterfaceId = toComponentHash shr 16
        val toComponent = toComponentHash and 0xFFFF

        log(client, "Swap component to component item: src_component=[%d, %d], dst_component=[%d, %d], src_item=%d, dst_item=%d",
                fromInterfaceId, fromComponent, toInterfaceId, toComponent, fromItemId, toItemId)

        client.attr[INTERACTING_ITEM_SLOT] = fromSlot
        client.attr[OTHER_ITEM_SLOT_ATTR] = toSlot

        val swapped = world.plugins.executeComponentToComponentItemSwap(
                client, fromInterfaceId, fromComponent, toInterfaceId, toComponent)

        if (!swapped && world.devContext.debugButtons) {
            client.message("Unhandled component to component swap: [from_item=$fromItemId, to_item=$toItemId, from_slot=$fromSlot, to_slot=$toSlot, " +
                    "from_component=[$fromInterfaceId, $fromComponent], to_component=[$toInterfaceId, $toComponent]]")
        }
    }
}