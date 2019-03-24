package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonDMessage
import gg.rsmod.game.model.attr.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.attr.OTHER_ITEM_SLOT_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButtonDHandler : MessageHandler<IfButtonDMessage> {

    override fun handle(client: Client, message: IfButtonDMessage) {
        val srcComponentHash = message.srcComponentHash
        val srcSlot = message.srcSlot
        val srcItemId = message.srcItem

        val dstComponentHash = message.dstComponentHash
        val dstSlot = message.dstSlot
        val dstItemId = message.dstItem

        val srcInterfaceId = srcComponentHash shr 16
        val srcComponent = srcComponentHash and 0xFFFF
        val dstInterfaceId = dstComponentHash shr 16
        val dstComponent = dstComponentHash and 0xFFFF

        log(client, "Swap component to component item: srcInterface=[%d, %d], srcItem=%d, dstInterface=[%d, %d], dstItem=%d",
                srcInterfaceId, srcComponent, srcItemId, dstInterfaceId, dstComponent, dstItemId)

        client.attr[INTERACTING_ITEM_SLOT] = srcSlot
        client.attr[OTHER_ITEM_SLOT_ATTR] = dstSlot

        val swapped = client.world.plugins.executeComponentToComponentItemSwap(
                client, srcInterfaceId, srcComponent, dstInterfaceId, dstComponent)

        if (!swapped && client.world.devContext.debugButtons) {
            client.message("Unhandled component to component swap: [fromSlot=$srcSlot, toSlot=$dstSlot, fromItem=$srcItemId, toItem=$dstItemId, fromInterface=[$srcInterfaceId, $srcComponent], toInterface=[$dstInterfaceId, $dstComponent]]")
        }
    }
}