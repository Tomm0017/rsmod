package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ObjectActionOneMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectActionOneHandler : MessageHandler<ObjectActionOneMessage> {

    override fun handle(client: Client, message: ObjectActionOneMessage) {
        client.world.server.getPlugins().executeObject(client, message.id, 1)
        log(client, "Object Action 1: id={}, x={}, z={}, movement={}", message.id, message.x, message.z, message.movementType)
    }
}