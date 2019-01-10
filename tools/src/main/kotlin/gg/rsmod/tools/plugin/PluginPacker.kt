package gg.rsmod.tools.plugin

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginPacker {

    fun compileSource(pluginName: String, outputPath: Path, paths: List<Path>, removeParent: String? = null): Boolean {
        return zipFiles(outputPath.resolve("$pluginName.zip"), paths, removeParent)
    }

    fun compileBinary(compilerPath: String, gameJar: String, pluginJar: String,
                      pluginName: String, outputPath: Path, paths: List<Path>): Boolean {
        val tmpOutput = outputPath.resolve("$pluginName.tmp")
        val output = outputPath.resolve("$pluginName.jar")

        val success = compileKotlin(compilerPath, gameJar, pluginJar, tmpOutput, paths)
        if (success) {
            try {
                zipFiles(output = output, paths = Files.walk(tmpOutput).toList(),
                        removeParent = tmpOutput.toString().replace("\\", "/"))
            } finally {
                tmpOutput.toFile().deleteRecursively()
            }
            return true
        }
        return false
    }

    private fun compileKotlin(compilerPath: String, gameJar: String, pluginJar: String, plugin: Path, paths: List<Path>): Boolean {
        val splitPaths = paths.filter { it.fileName.toString().endsWith(".kt") || it.fileName.toString().endsWith(".kts") }.joinToString(" ") { "\"$it\"" }

        val process = ProcessBuilder("$compilerPath/kotlinc.bat", "$splitPaths -classpath \"$gameJar\";$pluginJar -d \"$plugin\"").inheritIO()
        val status = process.start()
        status.waitFor(30, TimeUnit.SECONDS)
        status.destroyForcibly()


        return status.exitValue() == 0
    }

    private fun zipFiles(output: Path, paths: List<Path>, removeParent: String?): Boolean {
        val zip = ZipFile(output.toAbsolutePath().toString())
        val params = ZipParameters()
        params.compressionMethod = Zip4jConstants.COMP_DEFLATE
        params.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_ULTRA
        try {
            paths.forEach { path ->
                if (!Files.isDirectory(path)) {
                    val file = path.toFile()
                    params.rootFolderInZip = file.parent
                    if (removeParent != null) {
                        params.rootFolderInZip = params.rootFolderInZip.replace(removeParent, "")
                        if (params.rootFolderInZip.startsWith("/")) {
                            params.rootFolderInZip = params.rootFolderInZip.substring(1)
                        }
                    }
                    if (!params.rootFolderInZip.contains("META-INF")) {
                        zip.addFile(file, params)
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}