package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.UpdateIgnoreListMessage

class LoadIgnoreListEncoder : MessageEncoder<UpdateIgnoreListMessage>() {

    override fun extract(message: UpdateIgnoreListMessage, key: String): Number = when (key) {
        "online" -> message.online
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateIgnoreListMessage, key: String): ByteArray = when (key) {
        "username" -> {
            run {
                val data = ByteArray(message.username.length + 1)
                System.arraycopy(message.username.toByteArray(), 0, data, 0, data.size - 1)
                data[data.size - 1] = 0
                data
            }
        }

        "previousUsername" -> {
            run {
                val data = ByteArray(message.previousUsername.length + 1)
                System.arraycopy(message.previousUsername.toByteArray(), 0, data, 0, data.size - 1)
                data[data.size - 1] = 0
                data
            }
        }

        else -> throw Exception("Unhandled value key.")
    }
}