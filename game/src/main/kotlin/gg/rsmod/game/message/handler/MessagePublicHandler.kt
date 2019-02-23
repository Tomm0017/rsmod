package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MessagePublicMessage
import gg.rsmod.game.model.ChatMessage
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.sync.block.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicHandler : MessageHandler<MessagePublicMessage> {

    override fun handle(client: Client, message: MessagePublicMessage) {
        val decompressed = ByteArray(256)
        val huffman = client.world.huffman
        huffman.decompress(message.data, decompressed, message.length)

        val unpacked = String(decompressed, 0, message.length)
        val type = ChatMessage.ChatType.values.firstOrNull { it.id == message.type } ?: ChatMessage.ChatType.NONE
        val effect = ChatMessage.ChatEffect.values.firstOrNull { it.id == message.effect } ?: ChatMessage.ChatEffect.NONE
        val color = ChatMessage.ChatColor.values.firstOrNull { it.id == message.color } ?: ChatMessage.ChatColor.NONE

        client.blockBuffer.publicChat = ChatMessage(unpacked, client.privilege.icon, type, effect, color)
        client.addBlock(UpdateBlockType.PUBLIC_CHAT)
    }
}