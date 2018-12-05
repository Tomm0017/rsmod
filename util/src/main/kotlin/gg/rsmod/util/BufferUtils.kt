package gg.rsmod.util

import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
object BufferUtils {

    @JvmStatic fun readString(buf: ByteBuf): String {
        if (buf.isReadable) {
            val start = buf.readerIndex()
            while (buf.readByte().toInt() != 0);
            val size = buf.readerIndex() - start

            val data = ByteArray(size)
            buf.readerIndex(start)
            buf.readBytes(data)

            return String(data, 0, size - 1)
        } else {
            return ""
        }
    }

    fun readJagexString(buf: ByteBuf): String {
        if (buf.isReadable && buf.readByte().toInt() != 0) {
            val start = buf.readerIndex()
            while (buf.readByte().toInt() != 0);
            val size = buf.readerIndex() - start

            val data = ByteArray(size)
            buf.readerIndex(start)
            buf.readBytes(data)

            return String(data, 0, size - 1)
        } else {
            return ""
        }
    }
}