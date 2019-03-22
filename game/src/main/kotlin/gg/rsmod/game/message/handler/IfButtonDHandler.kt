package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IfButtonDMessage
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

        if (srcInterfaceId == 12 && dstInterfaceId == 12) {
            val container = client.bank

            val srcItem = container[srcSlot] ?: return
            val dstItem = container[dstSlot]

            if (srcItem.id != srcItemId || dstItem?.id != dstItemId) {
                return
            }

            container.swap(srcSlot, dstSlot)
        }
    }
}