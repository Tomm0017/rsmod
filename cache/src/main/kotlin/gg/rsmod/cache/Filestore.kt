package gg.rsmod.cache

import gg.rsmod.cache.config.CompressionType
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.ArchiveChunk
import gg.rsmod.cache.struct.Index
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.runelite.cache.util.BZip2
import net.runelite.cache.util.GZip
import java.util.Arrays

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Filestore internal constructor(
        internal val dataFile: CacheFile,
        internal val indexFiles: Map<Int, CacheFile>,
        internal val accessTypes: Set<CacheFiles.AccessType>) {

    fun load() {

        val archives = mutableMapOf<Int, Archive>()
        val archiveChunks = mutableMapOf<Int, ArchiveChunk>()
        val indexes = mutableMapOf<Int, Index>()

        // Load archive chunks
        val indexMaster = indexFiles.getValue(MASTER_IDX)
        val indexBuf = ByteArray(INDEX_ENTRY_LENGTH)
        val indexCount = (indexMaster.length() / INDEX_ENTRY_LENGTH).toInt()
        for (i in 0 until indexCount) {
            indexMaster.seek((i * INDEX_ENTRY_LENGTH).toLong())
            indexMaster.read(indexBuf)

            val length = ((indexBuf[0].toInt() and 0xFF) shl 16) or ((indexBuf[1].toInt() and 0xFF) shl 8) or ((indexBuf[2].toInt() and 0xFF))
            val offset = ((indexBuf[3].toInt() and 0xFF) shl 16) or ((indexBuf[4].toInt() and 0xFF) shl 8) or ((indexBuf[5].toInt() and 0xFF))

            check(length > 0)
            check(offset > 0)

            val chunk = ArchiveChunk(offset, length)
            archiveChunks[i] = chunk
        }

        // Load indexes
        val chunkBuffer = ByteArray(DATA_FILE_CHUNK_SIZE)
        archiveChunks.forEach { archiveIdx, chunk ->
            Arrays.fill(chunkBuffer, 0)

            val offset = chunk.offset
            val size = chunk.length
            val headerLength = 8

            val dataBuffer = readData(chunkBuffer, offset, size, headerLength).decompress()

            val protocol = dataBuffer.readUnsignedByte().toInt()
            check(protocol in 5..7)

            var revision = 0

            if (protocol >= 6) {
                revision = dataBuffer.readInt()
            }

            val hash = dataBuffer.readUnsignedByte().toInt()
            val hasHashName = (1 and hash) != 0
            check(hash and 1.inv() == 0)
            check(hash and 3.inv() == 0)

            val archiveCount = dataBuffer.readProtocolSmart(protocol)

            val index = Index(protocol, revision, hasHashName)
            indexes[archiveIdx] = index
        }
    }

    private fun readData(input: ByteArray, offset: Int, size: Int, headerLength: Int): ByteBuf {
        val output = Unpooled.buffer(size)

        var currOffset = offset
        var bytesRead = 0
        while (bytesRead < size) {
            dataFile.seek((currOffset * DATA_FILE_CHUNK_SIZE).toLong())

            var blockLength = size - bytesRead
            if (blockLength > DATA_FILE_CHUNK_SIZE - headerLength) {
                blockLength = DATA_FILE_CHUNK_SIZE - headerLength
            }

            val read = dataFile.read(input, 0, headerLength + blockLength)

            check(read == headerLength + blockLength)

            val nextOffset = ((input[4].toInt() and 0xFF) shl 16) or ((input[5].toInt() and 0xFF) shl 8) or
                    ((input[6].toInt() and 0xFF))

            output.writeBytes(input, headerLength, blockLength)

            currOffset = nextOffset
            bytesRead += blockLength
        }
        return output
    }

    private fun ByteBuf.decompress(): ByteBuf {
        val compression = readUnsignedByte().toInt()
        val length = readInt()

        val compressionType = CompressionType.values.first { it.value == compression }

        when (compressionType) {
            CompressionType.NONE -> {
                val encryptedData = ByteArray(length)
                readBytes(encryptedData)

                val decryptedData = encryptedData.copyOf()

                if (readableBytes() >= 2) {
                    readUnsignedShort()
                }

                return Unpooled.wrappedBuffer(decryptedData)
            }
            CompressionType.BZIP2 -> {
                val encryptedData = ByteArray(length + 4)
                readBytes(encryptedData)

                val decryptedData = encryptedData.copyOf()

                if (readableBytes() >= 2) {
                    readUnsignedShort()
                }

                val buffer = Unpooled.wrappedBuffer(decryptedData)
                val decompressedLength = buffer.readInt()

                val remaining = ByteArray(buffer.readableBytes())
                buffer.readBytes(remaining)
                val decompressedData = BZip2.decompress(remaining, length)

                return Unpooled.wrappedBuffer(decompressedData)
            }
            CompressionType.GZIP -> {
                val encryptedData = ByteArray(length + 4)
                readBytes(encryptedData)

                val decryptedData = encryptedData.copyOf()

                if (readableBytes() >= 2) {
                    readUnsignedShort()
                }

                val buffer = Unpooled.wrappedBuffer(decryptedData)
                val decompressedLength = buffer.readInt()

                val remaining = ByteArray(buffer.readableBytes())
                buffer.readBytes(remaining)
                val decompressedData = GZip.decompress(remaining, length)

                return Unpooled.wrappedBuffer(decompressedData)
            }
            else -> throw RuntimeException("Unhandled compression type: $compressionType")
        }
    }

    companion object {
        // The amount of bytes in each index entry.
        private const val INDEX_ENTRY_LENGTH = 6

        // The chunk size of each data file.
        private const val DATA_FILE_CHUNK_SIZE = 520
    }
}