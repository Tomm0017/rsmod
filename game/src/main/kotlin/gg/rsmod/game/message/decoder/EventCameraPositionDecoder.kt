package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.EventCameraPositionMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionDecoder : MessageDecoder<EventCameraPositionMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventCameraPositionMessage {
        val mouseX = values["pitch"]!!.toInt()
        val mouseY = values["yaw"]!!.toInt()
        return EventCameraPositionMessage(mouseX, mouseY)
    }
}