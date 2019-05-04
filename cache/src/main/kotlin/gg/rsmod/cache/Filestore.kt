package gg.rsmod.cache

import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Filestore private constructor(
        private val dataFile: RandomAccessFile,
        private val indexFiles: Map<Int, File>,
        private val accessTypes: Set<AccessType>) {

    /**
    yeah it's Index, Archive, Group

    index contains the metadata for an archive
    then the archive itself contains all of the groups

    yeah, but it's actually split up more than that technically still
    there's files in groups
    im not sure for the name of that "container" (i think is what openrs calls it)
    that is all of the files in a single buffer, compressed
     */

    companion object {

        private const val FILESTORE_PREFIX = "main_file_cache"
        private const val DATA_FILE_SUFFIX = "dat2"
        private const val IDX_FILE_SUFFIX = "idx"

        @JvmStatic fun main(vararg args: String) {
            val result = loadCache(Paths.get("./data", "cache"))
            println("Load cache result: $result")
        }

        fun loadCache(directory: Path): LoadCacheResult {
            // The access type(s) for our data file.
            val accessTypes = setOf(AccessType.READ, AccessType.WRITE)

            // Resolve the path to the main data file.
            val dataFile = directory.resolve("$FILESTORE_PREFIX.$DATA_FILE_SUFFIX")

            // If main data file is not found, we return the "no data file"
            // result.
            if (!Files.exists(dataFile)) {
                return LoadCacheResult(LoadResultType.NO_DATA_FILE)
            }

            // A map of idx files with their respective index id as their key.
            val indexFiles = mutableMapOf<Int, File>()

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
                indexFiles[idxNumber] = filePath.toFile()
            }

            // If no index file was present in the directory, we return the
            // "no index file" result.
            if (indexFiles.isEmpty()) {
                return LoadCacheResult(LoadResultType.NO_IDX_FILE)
            }

            // Create a RandomAccessFile for our data file.
            val dataRandomAccessFile = RandomAccessFile(dataFile.toFile(), AccessType.concatenate(accessTypes))
            val filestore = Filestore(dataRandomAccessFile, indexFiles.toMap(), accessTypes.toSet())

            // Return a successful state for our cache loading process.
            val result = LoadCacheResult(LoadResultType.SUCCESS)
            result.filestore = filestore
            return result
        }

        data class LoadCacheResult(val type: LoadResultType) {
            lateinit var filestore: Filestore
        }

        enum class LoadResultType {
            SUCCESS,
            NO_DATA_FILE,
            NO_IDX_FILE
        }

        private enum class AccessType(val flag: String) {
            READ("r"),
            WRITE("w");

            companion object {
                fun concatenate(types: Set<AccessType>): String = types.map { it.flag }.joinToString("")
            }
        }
    }
}