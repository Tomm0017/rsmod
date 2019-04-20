package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.MessagePublicMessage
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.GamePacketReader

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicDecoder : MessageDecoder<MessagePublicMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): MessagePublicMessage {
        throw RuntimeException()
    }

    override fun decode(opcode: Int, structure: MessageStructure, reader: GamePacketReader): MessagePublicMessage {
        val values = hashMapOf<String, Number>()
        structure.values.values.forEach { value ->
            if (value.signature == DataSignature.SIGNED) {
                values[value.id] = reader.getSigned(value.type, value.order, value.transformation)
            } else {
                values[value.id] = reader.getUnsigned(value.type, value.order, value.transformation)
            }
        }

        val type = values["type"]!!.toInt()
        val color = values["color"]!!.toInt()
        val effect = values["effect"]!!.toInt()
        val length = reader.unsignedSmart
        val data = ByteArray(reader.readableBytes)
        reader.getBytes(data)

        return MessagePublicMessage(type, color, effect, length, data)
    }
}