package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.MessagePrivateReceiverMessage

import io.github.oshai.kotlinlogging.KotlinLogging

class MessagePrivateReceiverEncoder : MessageEncoder<MessagePrivateReceiverMessage>() {

    override fun extract(message: MessagePrivateReceiverMessage, key: String): Number = when (key) {
        "unknown" -> message.unknown
        "unknown2" -> message.unknown2
        "rights" -> message.rights
        else -> throw Exception("Unhandled extract key: $key")
    }

    override fun extractBytes(message: MessagePrivateReceiverMessage, key: String): ByteArray = when (key) {
        "target" -> {
            run {
                val data = ByteArray(message.target.length + 1)
                System.arraycopy(message.target.toByteArray(), 0, data, 0, data.size - 1)
                data[data.size - 1] = 0
                data
            }
        }

        else -> throw Exception("Unhandled bytes key: $key")
    }
    companion object {
        private val logger = KotlinLogging.logger{}
    }
}