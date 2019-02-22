package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MessagePublicMessage
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
        client.blockBuffer.publicChat = unpacked
        client.blockBuffer.publicChatEffects = message.effect
        client.addBlock(UpdateBlockType.PUBLIC_CHAT)
        println("unpacked=$unpacked")
    }
}