package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.OpPlayerMessage

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayerEncoder : MessageEncoder<OpPlayerMessage>() {

    override fun extractBytes(message: OpPlayerMessage, key: String): ByteArray = when (key) {
        "option" -> {
            val option = message.option
            val data = ByteArray(option.length + 1)
            System.arraycopy(option.toByteArray(), 0, data, 0, data.size - 1)
            data[data.size - 1] = 0
            data
        }
        else -> throw Exception("Unhandled value key.")
    }

    override fun extract(message: OpPlayerMessage, key: String): Number = when (key) {
        "left_click" -> if (message.leftClick) 1 else 0
        "index" -> message.index
        else -> throw Exception("Unhandled value key.")
    }

}