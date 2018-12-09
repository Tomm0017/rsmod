package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.MessageStructure
import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.SetRunEnergyMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RunEnergyEncoder(override val structure: MessageStructure) : MessageEncoder<SetRunEnergyMessage>(structure) {

    override fun extract(message: SetRunEnergyMessage, key: String): Number = when (key) {
        "energy" -> message.energy
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SetRunEnergyMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}