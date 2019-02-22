package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MessagePublicMessage
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.util.TextUtil

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicHandler : MessageHandler<MessagePublicMessage> {

    override fun handle(client: Client, message: MessagePublicMessage) {
        val unpacked = TextUtil.decryptPlayerChat(message.data, message.length)
        client.blockBuffer.publicChat = unpacked
        client.blockBuffer.publicChatEffects = message.effect
        client.addBlock(UpdateBlockType.PUBLIC_CHAT)
    }
}