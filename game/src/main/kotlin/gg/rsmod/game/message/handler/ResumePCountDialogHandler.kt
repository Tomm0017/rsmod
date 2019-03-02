package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ResumePCountDialogMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePCountDialogHandler : MessageHandler<ResumePCountDialogMessage> {

    override fun handle(client: Client, message: ResumePCountDialogMessage) {
        log(client, "Integer input dialog: input=%d", message.input)
        client.queues.submitReturnValue(Math.max(0, message.input))
    }
}