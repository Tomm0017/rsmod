package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.CloseMainComponentMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainComponentHandler : MessageHandler<CloseMainComponentMessage> {

    override fun handle(client: Client, message: CloseMainComponentMessage) {
        client.closeMainComponent = true
    }
}