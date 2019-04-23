package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.EventAppletFocusMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventAppletFocusHandler : MessageHandler<EventAppletFocusMessage> {

    override fun handle(client: Client, world: World, message: EventAppletFocusMessage) {
        when (message.state) {
            FOCUSED_STATE -> client.appletFocused = true
            UNFOCUSED_STATE -> client.appletFocused = false
        }
    }

    companion object {
        private const val UNFOCUSED_STATE = 0
        private const val FOCUSED_STATE = 1
    }
}