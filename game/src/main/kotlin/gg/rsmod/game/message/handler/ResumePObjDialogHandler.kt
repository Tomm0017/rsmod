package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ResumePObjDialogMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePObjDialogHandler : MessageHandler<ResumePObjDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePObjDialogMessage) {
        log(client, "Searched item: item=%d", message.item)
        client.queues.submitReturnValue(message.item)
    }
}