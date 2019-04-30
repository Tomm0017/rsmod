package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.UpdateInvPartialMessage
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateInvPartialEncoder : MessageEncoder<UpdateInvPartialMessage>() {

    override fun extract(message: UpdateInvPartialMessage, key: String): Number = when (key) {
        "component_hash" -> message.componentHash
        "container_key" -> message.containerKey
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateInvPartialMessage, key: String): ByteArray = when (key) {
        "items" -> {

            /**
             * NOTE(Tom): this can change per revision, so figure out a way to
             * externalize the structure.
             */

            check(message.oldItems.size == message.newItems.size) { "Size of both `oldItems` and `newItems` must match." }
            val buf = GamePacketBuilder()
            val size = message.oldItems.size

            for (i in 0 until size) {
                val oldItem = message.oldItems[i]
                val newItem = message.newItems[i]

                val idMatch = oldItem?.id == newItem?.id
                val amountMatch = oldItem?.amount == newItem?.amount

                if (!idMatch || !amountMatch) {
                    buf.putSmart(i)
                    if (newItem != null) {
                        buf.put(DataType.SHORT, newItem.id + 1)
                        buf.put(DataType.BYTE, Math.min(255, newItem.amount))
                        if (newItem.amount >= 255) {
                            buf.put(DataType.INT, newItem.amount)
                        }
                    } else {
                        buf.put(DataType.SHORT, 0)
                    }
                }
            }
            val data = ByteArray(buf.byteBuf.readableBytes())
            buf.byteBuf.readBytes(data)
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}