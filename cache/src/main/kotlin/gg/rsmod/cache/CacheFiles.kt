package gg.rsmod.cache

import net.runelite.cache.fs.Store
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
object CacheFiles {

    @JvmStatic
    fun main(vararg args: String) {
        val cacheDirectory = Paths.get("./data", "cache")

        val store = Store(cacheDirectory.toFile())
        store.load()
        store.indexes[4].let { index ->
            index.toIndexData().archives.forEachIndexed { i, data ->
                println("Archive[$i]=(rev=${data.revision}, name=${data.nameHash}, crc=${data.crc}, fileCount=${data.files.size})")
            }
        }

        println()

        val result = find(cacheDirectory)
        check(result.type == CacheFiles.ResultType.SUCCESS)
        val filestore = result.filestore
        filestore.load()
    }

    private const val FILESTORE_PREFIX = "main_file_cache"
    private const val DATA_FILE_SUFFIX = "dat2"
    private const val IDX_FILE_SUFFIX = "idx"

    private val DEFAULT_ACCESS_TYPES = arrayOf(AccessType.READ)

    fun find(directory: Path, vararg accessType: AccessType = DEFAULT_ACCESS_TYPES): CacheFindResult {
        // A set of the access types given to the function.
        val accessTypes = accessType.toSet()
        val accessTypeFlags = AccessType.concatenate(accessTypes)

        // Resolve the path to the main data file.
        val dataFile = directory.resolve("$FILESTORE_PREFIX.$DATA_FILE_SUFFIX")

        // If main data file is not found, we return the "no data file"
        // result.
        if (!Files.exists(dataFile)) {
            return CacheFindResult(ResultType.NO_DATA_FILE)
        }

        // A map of idx files with their respective index id as their key.
        val indexFiles = mutableMapOf<Int, CacheFile>()

        // Go through each file in the directory to find index files.
        Files.walk(directory).forEach { filePath ->
            val fileName = filePath.fileName.toString()

            // Skip over any files that doesn't start with the appropriate
            // file name prefix
            if (!fileName.startsWith(FILESTORE_PREFIX)) {
                return@forEach
            }

            // Find the character index in the file name where the idx suffix
            // begins.
            val idxIndex = fileName.indexOf(".$IDX_FILE_SUFFIX")

            // If file name did not contain the idx file suffix, we skip
            // over it.
            if (idxIndex == -1) {
                return@forEach
            }

            // Resolve the index file number and store it.
            val idxNumber = fileName.substring(idxIndex + IDX_FILE_SUFFIX.length + 1).toInt()
            val idxCacheFile = CacheFile(filePath.toFile(), accessTypeFlags)

            indexFiles[idxNumber] = idxCacheFile
        }

        // If no index file was present in the directory, we return the
        // "no index file" result.
        if (indexFiles.isEmpty()) {
            return CacheFindResult(ResultType.NO_IDX_FILE)
        }

        // If the master index file is not found, we return the appropriate
        // failed result.
        if (!indexFiles.containsKey(MASTER_IDX)) {
            return CacheFindResult(ResultType.NO_MASTER_IDX_FILE)
        }

        // Create a RandomAccessFile for our data file.
        val dataCacheFile = CacheFile(dataFile.toFile(), accessTypeFlags)
        val filestore = Filestore(dataCacheFile, indexFiles.toMap(), accessTypes)

        // Return a successful state for our cache loading process.
        val result = CacheFindResult(ResultType.SUCCESS)
        result.filestore = filestore
        return result
    }

    data class CacheFindResult(val type: ResultType) {
        lateinit var filestore: Filestore
    }

    enum class ResultType {
        SUCCESS,
        NO_DATA_FILE,
        NO_IDX_FILE,
        NO_MASTER_IDX_FILE
    }

    enum class AccessType(val flag: String) {
        READ("r"),
        WRITE("w");

        companion object {
            fun concatenate(types: Set<AccessType>): String = types.joinToString("") { it.flag }
        }
    }
}