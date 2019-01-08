package gg.rsmod.game.service.game

import gg.rsmod.game.Server
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import net.runelite.cache.util.Namer
import org.apache.logging.log4j.LogManager
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionIdService : Service() {

    companion object {
        private val logger = LogManager.getLogger(DefinitionIdService::class.java)
    }

    private var cachePath: Path? = null

    private var outputPath: Path? = null

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val dump = serviceProperties.getOrDefault("dump", false)
        if (dump) {
            cachePath = Paths.get(serviceProperties.get<String>("cache-path")!!)
            outputPath = Paths.get(serviceProperties.getOrDefault("output-path", "./ids"))

            if (!Files.exists(outputPath)) {
                Files.createDirectory(outputPath)
                logger.info("Output path does not exist. Creating directory: $outputPath")
            } else if (!Files.isDirectory(outputPath)) {
                logger.error("Output path specified is a file - it must be a directory!")
            }
        }
    }

    override fun postLoad(server: Server, world: World) {
        val definitions = world.definitions

        val itemNamer = Namer()
        val itemCount = definitions.getCount(ItemDef::class.java)

        val items = generateWriter("Items.kt")
        for (i in 0 until itemCount) {
            val item = definitions.getNullable(ItemDef::class.java, i) ?: continue
            if (item.name.isNotBlank()) {
                val name = itemNamer.name(item.name, i)
                write(items, "const val $name = $i")
            }
        }
        endWriter(items)
    }

    private fun generateWriter(file: String): PrintWriter {
        val writer = PrintWriter(outputPath!!.resolve(file).toFile())
        writer.println("/* Auto-generated file */")
        writer.println("package gg.rsmod.plugins.osrs.api")
        writer.println("")
        writer.println("object Items {")
        writer.println("")
        return writer
    }

    private fun write(writer: PrintWriter, text: String) {
        writer.println("    $text")
    }

    private fun endWriter(writer: PrintWriter) {
        writer.println("}")
        writer.println("/* Auto-generated file */")
        writer.close()
    }

    override fun terminate(server: Server, world: World) {
    }
}