package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.DetectModifiedClientMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DetectModifiedClientHandler : MessageHandler<DetectModifiedClientMessage> {

    override fun handle(client: Client, world: World, message: DetectModifiedClientMessage) {
        log(client, "Detected modified client for player %s (%s).", client.username, client.channel)
    }
}