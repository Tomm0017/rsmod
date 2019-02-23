package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.impl.EventKeyboardMessage
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.GamePacketReader
import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventKeyboardDecoder : MessageDecoder<EventKeyboardMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventKeyboardMessage {
        throw RuntimeException()
    }

    override fun decode(opcode: Int, structure: MessageStructure, reader: GamePacketReader): EventKeyboardMessage {
        val events = ObjectArrayList<EventKeyboardMessage.KeyEvent>()
        while (reader.readableBytes > 0) {
            var pressedKey = -1
            var lastPress = -1

            structure.values.forEach { valueKey, value ->
                val data = if (value.signature == DataSignature.SIGNED) reader.getSigned(value.type, value.order, value.transformation)
                            else reader.getUnsigned(value.type, value.order, value.transformation)
                when (valueKey) {
                    "key" -> pressedKey = data.toInt()
                    "last_key_press" -> lastPress = data.toInt()
                    else -> throw IllegalArgumentException("Invalid key: $valueKey")
                }
            }

            events.add(EventKeyboardMessage.KeyEvent(pressedKey, lastPress))
        }
        return EventKeyboardMessage(events)
    }
}