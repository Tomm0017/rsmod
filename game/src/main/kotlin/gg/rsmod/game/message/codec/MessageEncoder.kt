package gg.rsmod.game.message.codec

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.GamePacketBuilder
import org.apache.logging.log4j.LogManager

/**
 * Responsible for encoding [Message]s into [GamePacket]s that can be sent to
 * the client.
 *
 * @param structure The [MessageStructure] that will be used to encode [T] into a
 * [GamePacket].
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class MessageEncoder<T: Message>(open val structure: MessageStructure) {

    companion object {
        private val logger = LogManager.getLogger(MessageEncoder::class.java)
    }

    /**
     * Encode the [message] into a [GamePacket] based on the [structure].
     */
    fun encode(message: T): GamePacket {
        val builder = GamePacketBuilder(structure.opcode, structure.type)
        structure.values.values.forEach { value ->
            if (value.type != DataType.BYTES) {
                builder.put(value.type, value.order, value.transformation, extract(message, value.id))
            } else {
                builder.putBytes(extractBytes(message, value.id))
            }
        }
        return builder.toGamePacket()
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