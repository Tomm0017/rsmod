package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.CloseMainInterfaceMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainInterfaceHandler : MessageHandler<CloseMainInterfaceMessage> {

    override fun handle(client: Client, message: CloseMainInterfaceMessage) {
        client.world.plugins.executeCloseMainInterface(client)
    }
}