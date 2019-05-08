package gg.rsmod.cache

import gg.rsmod.cache.config.CompressionType
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.ArchiveChunk
import gg.rsmod.cache.struct.Group
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
        val archiveChunks = mutableMapOf<Int, ArchiveChunk>()
        val indexes = mutableMapOf<Int, Index>()
        val archives = mutableMapOf<Index, MutableCollection<Archive>>()
        val groups = mutableMapOf<Archive, MutableCollection<Group>>()

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

            val revision = if (protocol >= 6) {
                dataBuffer.readInt()
            } else {
                0
            }

            val hash = dataBuffer.readUnsignedByte().toInt()
            val hasHashNames = (1 and hash) != 0
            check(hash and 1.inv() == 0)
            check(hash and 3.inv() == 0)

            val archiveCount = dataBuffer.readProtocolSmart(protocol)

            val index = Index(protocol, revision, hasHashNames)
            indexes[archiveIdx] = index

            val indexArchives = mutableListOf<Archive>()
            archives[index] = indexArchives

            val archiveIds = arrayOfNulls<Int>(archiveCount)
            val archiveNames = arrayOfNulls<Int>(archiveCount)
            val archiveCrcs = arrayOfNulls<Int>(archiveCount)
            val archiveRevisions = arrayOfNulls<Int>(archiveCount)

            val archiveGroupCount = arrayOfNulls<Int>(archiveCount)
            val archiveGroupIds = mutableMapOf<Archive, MutableMap<Int, Int>>()
            val archiveGroupNames = mutableMapOf<Archive, MutableMap<Int, Int>>()

            var incrementalProtocol = 0
            for (i in 0 until archiveCount) {
                incrementalProtocol += protocol
                archiveIds[i] = dataBuffer.readProtocolSmart(incrementalProtocol)
            }

            if (hasHashNames) {
                for (i in 0 until archiveCount) {
                    archiveNames[i] = dataBuffer.readInt()
                }
            }

            for (i in 0 until archiveCount) {
                archiveCrcs[i] = dataBuffer.readInt()
            }

            for (i in 0 until archiveCount) {
                archiveRevisions[i] = dataBuffer.readInt()
            }

            for (i in 0 until archiveCount) {
                archiveGroupCount[i] = dataBuffer.readProtocolSmart(protocol)
            }

            // Create archive based on metadata read from index file.
            for (i in 0 until archiveCount) {
                val archive = Archive(archiveIds[i]!!, archiveNames[i], archiveCrcs[i]!!, archiveRevisions[i]!!)
                indexArchives.add(archive)
            }

            // Associate the group ids found in the index file to the archive.
            for (i in 0 until archiveCount) {
                val groupCount = archiveGroupCount[i]!!
                val archive = indexArchives[i]
                val groupIds = archiveGroupIds.computeIfAbsent(archive) { mutableMapOf() }

                incrementalProtocol = 0
                for (j in 0 until groupCount) {
                    incrementalProtocol += protocol
                    groupIds[j] = dataBuffer.readProtocolSmart(incrementalProtocol)
                }
            }

            // If the index specified that the archive has hashed names, we
            // associate them with their respective archive.
            if (hasHashNames) {
                for (i in 0 until archiveCount) {
                    val groupCount = archiveGroupCount[i]!!
                    val archive = indexArchives[i]
                    val groupNames = archiveGroupNames.computeIfAbsent(archive) { mutableMapOf() }

                    for (j in 0 until groupCount) {
                        groupNames[j] = dataBuffer.readInt()
                    }
                }
            }

            // Create groups for archive.
            for (i in 0 until archiveCount) {
                val archive = indexArchives[i]
                val groupCount = archiveGroupCount[i]!!
                val groupIds = archiveGroupIds[archive]!!
                val groupNames = archiveGroupNames[archive]

                val archiveGroups = groups.computeIfAbsent(archive) { mutableListOf() }
                for (j in 0 until groupCount) {
                    val groupId = groupIds[j]!!
                    val groupName = groupNames?.get(j)
                    val group = Group(groupId, groupName)
                    archiveGroups.add(group)
                }
            }
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

            val nextOffset = ((input[4].toInt() and 0xFF) shl 16) or ((input[5].toInt() and 0xFF) shl 8) or ((input[6].toInt() and 0xFF))

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