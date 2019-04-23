package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ResumePauseButtonMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePauseButtonHandler : MessageHandler<ResumePauseButtonMessage> {

    override fun handle(client: Client, world: World, message: ResumePauseButtonMessage) {
        log(client, "Continue dialog: component=[%d, %d], slot=%d", message.interfaceId, message.component, message.slot)
        client.queues.submitReturnValue(message)
    }
}