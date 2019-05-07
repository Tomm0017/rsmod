package gg.rsmod.cache

import gg.rsmod.cache.config.ArchiveType
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.ArchiveChunk

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Filestore internal constructor(
        internal val dataFile: CacheFile,
        internal val indexFiles: Map<Int, CacheFile>,
        internal val accessTypes: Set<CacheFiles.AccessType>) {

    fun load() {

        val archives = mutableMapOf<ArchiveType, Archive>()
        val archiveChunks = mutableMapOf<ArchiveType, ArchiveChunk>()

        // Load archive chunks
        val indexMaster = indexFiles.getValue(MASTER_IDX)
        val indexBuf = ByteArray(INDEX_ENTRY_LENGTH)
        ArchiveType.values.forEach { archiveType ->
            val archiveIdx = archiveType.idx
            indexMaster.seek((archiveIdx * INDEX_ENTRY_LENGTH).toLong())
            indexMaster.read(indexBuf)

            val length = ((indexBuf[0].toInt() and 0xFF) shl 16) or ((indexBuf[1].toInt() and 0xFF) shl 8) or ((indexBuf[2].toInt() and 0xFF))
            val offset = ((indexBuf[3].toInt() and 0xFF) shl 16) or ((indexBuf[4].toInt() and 0xFF) shl 8) or ((indexBuf[5].toInt() and 0xFF))

            check(length > 0)
            check(offset > 0)

            val chunk = ArchiveChunk(offset, length)
            archiveChunks[archiveType] = chunk
        }

        // Load indexes
        val dataBuffer = ByteArray(DATA_FILE_CHUNK_SIZE)
        ArchiveType.values.forEach { archiveType ->
            val chunk = archiveChunks.getValue(archiveType)
            val indexId = MASTER_IDX
            val archiveId = archiveType.idx
            val sector = chunk.offset
            val size = chunk.length


        }
    }

    companion object {
        // The amount of bytes in each index entry.
        private const val INDEX_ENTRY_LENGTH = 6

        // The chunk size of each data file.
        private const val DATA_FILE_CHUNK_SIZE = 520
    }
}