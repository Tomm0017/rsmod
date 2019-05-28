package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ResumePNameDialogMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.queue.QueueTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePNameDialogHandler : MessageHandler<ResumePNameDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePNameDialogMessage) {
        val name = message.name
        val target = world.getPlayerForName(name)

        log(client, "Player username input dialog: username=%s", name)

        client.queues.submitReturnValue(target ?: QueueTask.EMPTY_RETURN_VALUE)
    }
}