package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.WindowStatusMessage
import gg.rsmod.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DisplayModeHandler : MessageHandler<WindowStatusMessage> {

    override fun handle(client: Client, message: WindowStatusMessage) {
        client.attr[DISPLAY_MODE_CHANGE_ATTR] = message.mode
        client.world.plugins.executeDisplayModeChange(client)
    }
}