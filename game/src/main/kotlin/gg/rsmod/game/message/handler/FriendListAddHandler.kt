package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.FriendListAddMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

class FriendListAddHandler : MessageHandler<FriendListAddMessage> {
    override fun handle(client: Client, world: World, message: FriendListAddMessage) {
        client.social.addFriend(client, message.username)
    }
}