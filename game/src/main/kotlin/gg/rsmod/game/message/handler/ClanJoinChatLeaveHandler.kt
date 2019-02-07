package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClanJoinChatLeaveChatMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClanJoinChatLeaveHandler : MessageHandler<ClanJoinChatLeaveChatMessage> {

    override fun handle(client: Client, message: ClanJoinChatLeaveChatMessage) {
        throw RuntimeException("Unhandled.")
    }
}