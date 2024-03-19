package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.IgnoreListAddMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

class IgnoreListAddHandler : MessageHandler<IgnoreListAddMessage> {
    override fun handle(client: Client, world: World, message: IgnoreListAddMessage) {
        client.social.addIgnore(client, message.username)
    }
}