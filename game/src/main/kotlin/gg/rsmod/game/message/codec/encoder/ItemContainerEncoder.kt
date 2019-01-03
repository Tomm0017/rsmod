package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetItemContainerMessage
import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainerEncoder : MessageEncoder<SetItemContainerMessage>() {

    override fun extract(message: SetItemContainerMessage, key: String): Number = when (key) {
        "interfaceHash" -> message.interfaceHash
        "containerKey" -> message.containerKey
        "itemCount" -> message.items.size
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetItemContainerMessage, key: String): ByteArray = when (key) {
        "items" -> {
            val buf = GamePacketBuilder()
            message.items.forEach { item ->
                /**
                 * TODO(Tom): this can change per revision, so figure out a way
                 * to externalize the structure.
                 */
                if (item != null) {
                    buf.put(DataType.SHORT, item.id + 1)
                    buf.put(DataType.BYTE, Math.min(255, item.amount))
                    if (item.amount >= 255) {
                        buf.put(DataType.INT, DataOrder.INVERSED_MIDDLE, item.amount)
                    }
                } else {
                    buf.put(DataType.SHORT, 0)
                    buf.put(DataType.BYTE, 0)
                }
            }
            val data = ByteArray(buf.getBuffer().readableBytes())
            buf.getBuffer().readBytes(data)
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}