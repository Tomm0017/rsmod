package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.MessagePrivateSenderMessage
import gg.rsmod.net.packet.GamePacketReader
import java.lang.RuntimeException

class MessagePrivateSenderDecoder : MessageDecoder<MessagePrivateSenderMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): MessagePrivateSenderMessage {
        throw RuntimeException()
    }

    override fun decode(
        opcode: Int,
        structure: MessageStructure,
        reader: GamePacketReader
    ): MessagePrivateSenderMessage {
        val target = reader.string
        val length = reader.unsignedSmart
        val data = ByteArray(reader.readableBytes)
        reader.getBytes(data)
        return MessagePrivateSenderMessage(target, length, data)
    }
}