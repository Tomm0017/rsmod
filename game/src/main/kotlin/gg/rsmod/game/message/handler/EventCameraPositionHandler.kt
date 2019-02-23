package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.EventCameraPositionMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionHandler : MessageHandler<EventCameraPositionMessage> {

    override fun handle(client: Client, message: EventCameraPositionMessage) {
        client.cameraPitch = message.pitch
        client.cameraYaw = message.yaw
    }
}