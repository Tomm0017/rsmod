package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.CloseModalMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainComponentHandler : MessageHandler<CloseModalMessage> {

    override fun handle(client: Client, message: CloseModalMessage) {
        if (client.interfaces.getModal() != -1) {
            client.interfaces.close(client.interfaces.getModal())
            client.interfaces.setModal(-1)
        }
    }
}