package gg.rsmod.net.codec.handshake

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class HandshakeType(val id: Int) {
    LOGIN(14),
    FILESTORE(15);

    companion object {
        val values = enumValues<HandshakeType>()
    }
}