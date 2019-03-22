package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpHeldDMessage
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

        val isInventory = interfaceId == 149 && component == 0

        if (isInventory) {
            val container = client.inventory
            if (srcSlot in 0..container.capacity && dstSlot in 0..container.capacity) {
                container.swap(srcSlot, dstSlot)
            }
        }
    }
}