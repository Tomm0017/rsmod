package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.message.impl.ChangeDisplayModeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DisplayModeHandler : MessageHandler<ChangeDisplayModeMessage> {

    override fun handle(client: Client, message: ChangeDisplayModeMessage) {
    }
}