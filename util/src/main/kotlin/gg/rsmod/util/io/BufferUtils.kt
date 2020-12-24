package gg.rsmod.util.io

import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
object BufferUtils {
    fun ByteBuf.readString(): String {
        if (isReadable) {
            val start = readerIndex()
            while (readByte().toInt() != 0);
            val size = readerIndex() - start

            val data = ByteArray(size)
            readerIndex(start)
            readBytes(data)

            return String(data, 0, size - 1)
        } else {
            return ""
        }
    }

    fun ByteBuf.readJagexString(): String {
        if (isReadable && readByte().toInt() != 0) {
            val start = readerIndex()
            while (readByte().toInt() != 0);
            val size = readerIndex() - start

            val data = ByteArray(size)
            readerIndex(start)
            readBytes(data)

            return String(data, 0, size - 1)
        } else {
            return ""
        }
    }

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * in Litte endian format (DCBA)
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     *
     * Note| this method now is natively implemented in netty and shouldn't be needed
     *       commented out for posterity and to keep it from showing in code completion
     */
    fun ByteBuf.readLEInt(): Int {
        if(readableBytes() < 4) throw IndexOutOfBoundsException("buffer does not contain enough bytes to read an int")
        return (readByte().toInt() and 0xFF) + ((readByte().toInt() and 0xFF) shl 8) + ((readByte().toInt() and 0xFF) shl 16) + ((readByte().toInt() and 0xFF) shl 24)
    }

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * in Inverse Middle endian format (BADC)
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    fun ByteBuf.readInverseMiddleInt(): Int {
        if(readableBytes() < 4) throw IndexOutOfBoundsException("buffer does not contain enough bytes to read an int")
        return ((readByte().toInt() and 0xFF) shl 16) + ((readByte().toInt() and 0xFF) shl 24) + (readByte().toInt() and 0xFF) + ((readByte().toInt() and 0xFF) shl 8)
    }

    /**
     * Gets a 32-bit integer at the current {@code readerIndex}
     * in Middle endian format (CDAB)
     * and increases the {@code readerIndex} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if {@code this.readableBytes} is less than {@code 4}
     */
    fun ByteBuf.readMiddleEndianInt(): Int {
        if(readableBytes() < 4) throw IndexOutOfBoundsException("buffer does not contain enough bytes to read an int")
        return ((readByte().toInt() and 0xFF) shl 8) + (readByte().toInt() and 0xFF) + ((readByte().toInt() and 0xFF) shl 24) + ((readByte().toInt() and 0xFF) shl 16)
    }

}