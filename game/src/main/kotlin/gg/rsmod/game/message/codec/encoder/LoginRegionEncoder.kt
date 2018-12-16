package gg.rsmod.game.message.codec.encoder

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.impl.ChangeStaticRegionMessage
import gg.rsmod.game.message.impl.LoginRegionMessage
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * Responsible for extracting values from the [LoginRegionMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginRegionEncoder : MessageEncoder<LoginRegionMessage>() {

    override fun extract(message: LoginRegionMessage, key: String): Number = when (key) {
        "x" -> message.tile.x shr 3
        "z" -> message.tile.z shr 3
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LoginRegionMessage, key: String): ByteArray = when (key) {
        "gpi" -> {
            val buf = GamePacketBuilder()

            buf.switchToBitAccess()
            buf.putBits(30, message.tile.to30BitInteger())
            for (i in 1 until 2048) {
                if (i != message.playerIndex) {
                    buf.putBits(18, 0)
                }
            }
            buf.switchToByteAccess()

            val gpi = ByteArray(buf.getBuffer().readableBytes())
            buf.getBuffer().readBytes(gpi)

            gpi
        }
        /**
         * Since the xtea payload is exactly the same as the [ChangeStaticRegionMessage], let's reuse it.
         */
        "xteas" -> ChangeStaticRegionEncoder().extractBytes(ChangeStaticRegionMessage(message.tile.x, message.tile.z, message.xteaKeyService), key)
        else -> throw Exception("Unhandled value key.")
    }
}