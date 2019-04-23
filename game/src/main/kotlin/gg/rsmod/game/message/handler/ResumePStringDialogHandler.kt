package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ResumePStringDialogMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePStringDialogHandler : MessageHandler<ResumePStringDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePStringDialogMessage) {
        log(client, "String input dialog: input=%d", message.input)
        client.queues.submitReturnValue(message.input)
    }
}