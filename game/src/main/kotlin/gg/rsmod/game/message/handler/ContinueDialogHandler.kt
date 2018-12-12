package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ContinueDialogMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ContinueDialogHandler : MessageHandler<ContinueDialogMessage> {

    override fun handle(client: Client, message: ContinueDialogMessage) {
        log(client, "Continue Dialog: parent={}, child={}, slot={}", message.parent, message.child, message.slot)
        client.world.pluginExecutor.submitReturnType(client, message.slot)
    }
}