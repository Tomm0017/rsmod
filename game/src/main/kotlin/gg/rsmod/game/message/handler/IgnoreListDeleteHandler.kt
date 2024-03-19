package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IgnoreListDeleteMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

class IgnoreListDeleteHandler : MessageHandler<IgnoreListDeleteMessage> {
    override fun handle(client: Client, world: World, message: IgnoreListDeleteMessage) {
        client.social.deleteIgnore(client, message.username)
    }
}