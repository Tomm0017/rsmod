package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.WindowStatusMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WindowStatusHandler : MessageHandler<WindowStatusMessage> {

    override fun handle(client: Client, world: World, message: WindowStatusMessage) {
        client.clientWidth = message.width
        client.clientHeight = message.height
        client.attr[DISPLAY_MODE_CHANGE_ATTR] = message.mode
        world.plugins.executeWindowStatus(client)
    }
}