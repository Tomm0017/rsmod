package gg.rsmod.cache.ext

import gg.rsmod.cache.type.CompressionType
import gg.rsmod.cache.error.FilestoreException
import gg.rsmod.cache.error.compress.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.runelite.cache.util.BZip2
import net.runelite.cache.util.GZip
import java.util.zip.CRC32

/**
 * The valid length of compression data, in bytes.
 */
private val VALID_COMPRESSION_LENGTH = 0..1_000_000

fun ByteBuf.readBigSmart(): Int = if (getByte(readerIndex()) >= 0) {
    readUnsignedShort() and 0xFFFF
} else {
    readInt() and Int.MAX_VALUE
}

fun ByteBuf.readProtocolSmart(protocol: Int): Int = if (protocol >= 7) {
    readBigSmart()
} else {
    readUnsignedShort()
}

@Throws(FilestoreException::class)
fun ByteBuf.decompress(crc: CRC32): ByteBuf {
    val compression = readUnsignedByte().toInt()
    val length = readInt()

    val validLength = VALID_COMPRESSION_LENGTH
    if (length !in validLength) {
        throw CompressedLengthOutOfBoundsException(validLength.first, validLength.last)
    }

    crc.update(array(), 0, 5) // update compression & length

    val compressionType = CompressionType.values.firstOrNull { it.value == compression }

    when (compressionType) {
        CompressionType.NONE -> {
            val encryptedData = ByteArray(length)
            readBytes(encryptedData)

            val decryptedData = encryptedData.copyOf()

            if (readableBytes() >= Short.SIZE_BYTES) {
                val revision = readUnsignedShort()
                if (revision == -1) {
                    throw IllegalCompressedRevisionException(revision)
                }
            }

            return Unpooled.wrappedBuffer(decryptedData)
        }

        CompressionType.BZIP2 -> {
            val encryptedData = ByteArray(length + 4)
            readBytes(encryptedData)

            val decryptedData = encryptedData.copyOf()

            if (readableBytes() >= Short.SIZE_BYTES) {
                val revision = readUnsignedShort()
                if (revision == -1) {
                    throw IllegalCompressedRevisionException(revision)
                }
            }

            val buffer = Unpooled.wrappedBuffer(decryptedData)
            val decompressedLength = buffer.readInt()

            val remaining = ByteArray(buffer.readableBytes())
            buffer.readBytes(remaining)
            val decompressedData = BZip2.decompress(remaining, length) ?: throw InvalidDecompressedDataException()

            if (decompressedData.size != decompressedLength) {
                throw DecompressedLengthMismatchException(decompressedData.size, decompressedLength)
            }

            return Unpooled.wrappedBuffer(decompressedData)
        }

        CompressionType.GZIP -> {
            val encryptedData = ByteArray(length + 4)
            readBytes(encryptedData)

            val decryptedData = encryptedData.copyOf()

            if (readableBytes() >= Short.SIZE_BYTES) {
                val revision = readUnsignedShort()
                if (revision == -1) {
                    throw IllegalCompressedRevisionException(revision)
                }
            }

            val buffer = Unpooled.wrappedBuffer(decryptedData)
            val decompressedLength = buffer.readInt()

            val remaining = ByteArray(buffer.readableBytes())
            buffer.readBytes(remaining)
            val decompressedData = GZip.decompress(remaining, length) ?: throw InvalidDecompressedDataException()

            if (decompressedData.size != decompressedLength) {
                throw DecompressedLengthMismatchException(decompressedData.size, decompressedLength)
            }

            return Unpooled.wrappedBuffer(decompressedData)
        }

        else -> throw IllegalCompressionTypeException(compressionType)
    }
}