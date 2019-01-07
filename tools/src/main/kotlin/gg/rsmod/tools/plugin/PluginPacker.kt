package gg.rsmod.tools.plugin

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginPacker {

    /**
     * Alias for [zipFiles]
     */
    fun compileSource(output: Path, paths: List<Path>) {
        zipFiles(output, paths)
    }

    fun compileBinary(compilerPath: String, gameJar: String, plugin: Path, paths: List<Path>): Boolean {
        val splitPaths = paths.filter { it.fileName.toString().endsWith(".kt") }.joinToString(" ") { "\"$it\"" }

        val process = ProcessBuilder("$compilerPath/kotlinc.bat", "$splitPaths -classpath \"$gameJar\" -d \"$plugin\"")
        val status = process.start()
        status.waitFor()
        status.destroyForcibly()

        return status.exitValue() == 0
    }

    fun zipFiles(output: Path, paths: List<Path>, removeParent: String? = null) {
        val zip = ZipFile(output.toAbsolutePath().toString())
        val params = ZipParameters()
        params.compressionMethod = Zip4jConstants.COMP_DEFLATE
        params.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_ULTRA
        paths.forEach { path ->
            if (!Files.isDirectory(path)) {
                val file = path.toFile()
                params.rootFolderInZip = file.parent
                if (removeParent != null) {
                    params.rootFolderInZip = params.rootFolderInZip.replace(removeParent, "").substring(1)
                }
                if (!params.rootFolderInZip.contains("META-INF")) {
                    zip.addFile(file, params)
                }
            }
        }
    }
}