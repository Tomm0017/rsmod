package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IntegerInputMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IntegerInputHandler : MessageHandler<IntegerInputMessage> {

    override fun handle(client: Client, message: IntegerInputMessage) {
        log(client, "Input integer: input=%d", message.input)
        client.world.pluginExecutor.submitReturnType(client, Math.max(0, message.input))
    }
}