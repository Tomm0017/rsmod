package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ConfirmDisplayNameMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ConfirmDisplayNameHandler : MessageHandler<ConfirmDisplayNameMessage> {

    override fun handle(client: Client, message: ConfirmDisplayNameMessage) {
        throw RuntimeException("Unhandled.")
    }
}