package gg.rsmod.tools.plugin

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginPacker {

    companion object {
        @JvmStatic fun main(vararg args: String) {
            val packer = PluginPacker()

            val source = Paths.get("game", "src", "main", "kotlin", "gg", "rsmod", "plugins", "osrs", "content", "skills", "prayer")
            val destination = Paths.get(".", "data", "plugins")
            val files = Files.walk(source).toList()

            packer.compileSource(destination.resolve("prayer.zip"), files)
        }
    }

    fun compileSource(plugin: Path, paths: List<Path>) {
        val zip = ZipFile(plugin.toAbsolutePath().toString())
        val params = ZipParameters()
        params.compressionMethod = Zip4jConstants.COMP_DEFLATE
        params.compressionLevel = Zip4jConstants.DEFLATE_LEVEL_ULTRA
        paths.forEach { path ->
            if (!Files.isDirectory(path)) {
                val file = path.toFile()
                params.rootFolderInZip = file.parent
                zip.addFile(file, params)
            }
        }
    }

    fun compileBinary(plugin: Path, paths: List<Path>) {
        // TODO(Tom): compile to .jar with only class files
    }
}