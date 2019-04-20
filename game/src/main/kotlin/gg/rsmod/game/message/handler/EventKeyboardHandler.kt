package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.EventKeyboardMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventKeyboardHandler : MessageHandler<EventKeyboardMessage> {

    override fun handle(client: Client, message: EventKeyboardMessage) {
    }
}