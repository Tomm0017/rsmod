package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldUMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldUHandler : MessageHandler<OpHeldUMessage> {

    override fun handle(client: Client, message: OpHeldUMessage) {
        val fromComponentHash = message.fromComponentHash
        val fromInterfaceId = fromComponentHash shr 16
        val fromComponent = fromComponentHash and 0xFFFF
        val fromSlot = message.fromSlot
        val fromItemId = message.fromItem

        val toComponentHash = message.fromComponentHash
        val toInterfaceId = toComponentHash shr 16
        val toComponent = toComponentHash and 0xFFFF
        val toSlot = message.toSlot
        val toItemId = message.toItem

        val fromItem = client.inventory[fromSlot] ?: return
        val toItem = client.inventory[toSlot] ?: return

        if (fromItem.id != fromItemId || toItem.id != toItemId) {
            return
        }

        if (!client.lock.canItemInteract()) {
            return
        }

        val handled = client.world.plugins.executeItemOnItem(client, fromItem.id, toItem.id)
        if (!handled && client.world.devContext.debugItemActions) {
            client.message("Unhandled item on item: [fromItem=${fromItem.id}, toItem=${toItem.id}, fromSlot=$fromSlot, toSlot=$toSlot, " +
                    "fromComponent=[$fromInterfaceId, $fromComponent], toComponent=[$toInterfaceId, $toComponent]]")
        }
    }
}