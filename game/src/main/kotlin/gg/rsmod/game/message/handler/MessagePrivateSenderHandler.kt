package gg.rsmod.game.message.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.MessagePrivateSenderMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @TODO
 */
class MessagePrivateSenderHandler : MessageHandler<MessagePrivateSenderMessage> {
    override fun handle(client: Client, world: World, message: MessagePrivateSenderMessage) {
        val decompressed = ByteArray(230)
        val huffman = world.huffman
        huffman.decompress(message.message, decompressed, message.length)
        val unpacked = String(decompressed, 0, message.length)

        logger.info { "Sender: ${client.username} - Target: ${message.target} - Message: [${message.length}] $unpacked" }

        val target = world.getPlayerForName(message.target)
        if (target != null) {
            logger.info { "Attempting to send packet to target" }
            client.social.sendPrivateMessage(client, target, message.length, message.message)
        }
    }
    companion object {
        private val logger = KotlinLogging.logger{}
    }
}