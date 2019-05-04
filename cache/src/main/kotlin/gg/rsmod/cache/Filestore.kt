package gg.rsmod.cache

import gg.rsmod.cache.config.IndexType
import gg.rsmod.cache.struct.Archive
import java.io.File
import java.io.RandomAccessFile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Filestore internal constructor(
        private val dataFile: RandomAccessFile,
        private val indexFiles: Map<Int, File>,
        private val accessTypes: Set<CacheFiles.AccessType>) {

    internal val archives = mutableMapOf<IndexType, Archive>()

    internal val referenceIndex: File
        get() = indexFiles.getValue(REFERENCE_IDX)

    companion object {
        // The idx id that holds the version references.
        internal const val REFERENCE_IDX = 255
    }
}