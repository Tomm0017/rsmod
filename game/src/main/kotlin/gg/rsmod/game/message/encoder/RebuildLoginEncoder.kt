package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.RebuildLoginMessage
import gg.rsmod.game.message.impl.RebuildNormalMessage
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * Responsible for extracting values from the [RebuildLoginMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class RebuildLoginEncoder : MessageEncoder<RebuildLoginMessage>() {

    override fun extract(message: RebuildLoginMessage, key: String): Number = when (key) {
        "x" -> message.tile.x shr 3
        "z" -> message.tile.z shr 3
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: RebuildLoginMessage, key: String): ByteArray = when (key) {
        "gpi" -> {
            val buf = GamePacketBuilder()

            buf.switchToBitAccess()
            buf.putBits(30, message.tile.as30BitInteger)
            for (i in 1 until 2048) {
                if (i != message.playerIndex) {
                    buf.putBits(18, message.playerTiles[i])
                }
            }
            buf.switchToByteAccess()

            val gpi = ByteArray(buf.byteBuf.readableBytes())
            buf.byteBuf.readBytes(gpi)

            gpi
        }
        /**
         * Since the xtea payload is exactly the same as the [RebuildNormalMessage], let's reuse it.
         */
        "xteas" -> RebuildNormalEncoder().extractBytes(RebuildNormalMessage(message.tile.x shr 3, message.tile.z shr 3, message.xteaKeyService), key)
        else -> throw Exception("Unhandled value key.")
    }
}