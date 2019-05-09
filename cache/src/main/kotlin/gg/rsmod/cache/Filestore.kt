package gg.rsmod.cache

import gg.rsmod.cache.codec.impl.ArchiveChunkCodec
import gg.rsmod.cache.codec.impl.ArchiveGroupListCodec
import gg.rsmod.cache.codec.impl.ArchiveListCodec
import gg.rsmod.cache.codec.impl.IndexCodec
import gg.rsmod.cache.ext.decompress
import gg.rsmod.cache.ext.readChunks
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.ArchiveChunk
import gg.rsmod.cache.struct.Group
import gg.rsmod.cache.struct.Index
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.util.Arrays
import java.util.zip.CRC32

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Filestore internal constructor(
        internal val dataFile: CacheFile,
        internal val indexFiles: Map<Int, CacheFile>,
        internal val accessTypes: Set<CacheFiles.AccessType>) {

    /* Step-by-step
     * 1) Load offset & length of archives in master index file
     * 2) Load index metadata from offset & lengths of archives
     * 3) Iterate through indexes > load metadata for archives and metadata for all their groups
     */

    internal lateinit var vfs: VirtualFileSystem

    internal fun loadVFS(): VirtualFileSystem {
        val masterIndex = indexFiles.getValue(MASTER_IDX)

        val archiveChunks = loadArchiveChunks(masterIndex)
        val indexes = loadIndexes(archiveChunks)

        val indexArchives = mutableMapOf<Index, List<Archive>>()
        val archiveGroups = mutableMapOf<Archive, List<Group>>()

        indexes.forEach { _, index ->
            val buffer = Unpooled.wrappedBuffer(index.archiveData)
            val archives = loadArchives(buffer, index)
            val groups = loadArchiveGroups(buffer, index, archives)

            indexArchives[index] = archives
            archives.forEach { archive ->
                val group = groups.getValue(archive)
                archiveGroups[archive] = group
            }
        }

        return VirtualFileSystem(indexes, indexArchives, archiveGroups)
    }

    private fun loadArchiveChunks(indexFile: CacheFile): Map<Int, ArchiveChunk> {
        val chunks = mutableMapOf<Int, ArchiveChunk>()

        val indexCount = (indexFile.length() / INDEX_ENTRY_LENGTH).toInt()
        val indexData = ByteArray(INDEX_ENTRY_LENGTH)
        val indexBuffer = Unpooled.wrappedBuffer(indexData)

        for (i in 0 until indexCount) {
            indexFile.seek((i * Filestore.INDEX_ENTRY_LENGTH).toLong())
            indexFile.read(indexData)

            val chunk = ArchiveChunkCodec.decode(indexBuffer.resetReaderIndex())
            chunks[i] = chunk
        }
        return chunks
    }

    private fun loadIndexes(archiveChunks: Map<Int, ArchiveChunk>): Map<Int, Index> {
        val indexes = mutableMapOf<Int, Index>()

        val data = ByteArray(DATA_FILE_CHUNK_SIZE)

        archiveChunks.forEach { archiveIdx, chunk ->
            Arrays.fill(data, 0)

            val offset = chunk.offset
            val length = chunk.length

            val crc = CRC32()
            val decompressed = data.readChunks(dataFile, offset, length, 8).decompress(crc)

            val index = IndexCodec.decode(decompressed)
            indexes[archiveIdx] = index
        }


        return indexes
    }

    private fun loadArchives(archiveBuf: ByteBuf, index: Index): List<Archive> {
        return ArchiveListCodec(index.versionType, index.nameHash).decode(archiveBuf)
    }

    private fun loadArchiveGroups(archiveBuf: ByteBuf, index: Index, archives: List<Archive>): Map<Archive, List<Group>> {
        return ArchiveGroupListCodec(archives, index.versionType, index.nameHash).decode(archiveBuf)
    }

    companion object {
        // The amount of bytes in each index entry.
        const val INDEX_ENTRY_LENGTH = 6

        // The chunk size of each data file.
        const val DATA_FILE_CHUNK_SIZE = 520
    }
}