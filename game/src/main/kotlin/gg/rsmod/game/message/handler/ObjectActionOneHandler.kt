package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ObjectActionOneMessage
import gg.rsmod.game.message.impl.SendChatboxTextMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectActionOneHandler : MessageHandler<ObjectActionOneMessage> {

    override fun handle(client: Client, message: ObjectActionOneMessage) {
        log(client, "Object Action 1: id={}, x={}, z={}, movement={}", message.id, message.x, message.z, message.movementType)

        client.interruptPlugins()
        if (client.world.plugins.executeObject(client, message.id, 1)) {
            return
        }

        if (client.world.gameContext.devMode) {
            val debug = "Unhandled object action: [opt=1, id=${message.id}, x=${message.x}, z=${message.z}]"
            client.write(SendChatboxTextMessage(type = 0, message = debug, username = null))
        }
    }
}