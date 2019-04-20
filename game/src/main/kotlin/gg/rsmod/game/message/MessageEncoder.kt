package gg.rsmod.game.message

import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * Responsible for encoding [Message]s into [GamePacket]s that can be sent to
 * the client.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class MessageEncoder<T : Message> {

    /**
     * Writes data from the [message] into [builder] based on the [structure].
     */
    fun encode(message: T, builder: GamePacketBuilder, structure: MessageStructure) {
        structure.values.values.forEach { value ->
            if (value.type != DataType.BYTES) {
                builder.put(value.type, value.order, value.transformation, extract(message, value.id))
            } else {
                builder.putBytes(extractBytes(message, value.id))
            }
        }
    }

    /**
     * Get the [Number] value based on the [key].
     */
    abstract fun extract(message: T, key: String): Number

    /**
     * Get the [ByteArray] value based on the [key].
     */
    abstract fun extractBytes(message: T, key: String): ByteArray
}