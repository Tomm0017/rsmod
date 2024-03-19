package gg.rsmod.game.service.game

import dev.openrune.cache.CacheManager.item
import dev.openrune.cache.CacheManager.itemCount
import dev.openrune.cache.CacheManager.npc
import dev.openrune.cache.CacheManager.npcCount
import dev.openrune.cache.CacheManager.objectCount
import dev.openrune.cache.CacheManager.objects
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import net.runelite.cache.util.Namer
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DumpEntityIdService : Service {

    private var dump = false

    private var cachePath: Path? = null

    private var outputPath: Path? = null

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        dump = serviceProperties.getOrDefault("dump", false)
        if (dump) {
            cachePath = Paths.get(serviceProperties.get<String>("cache-path")!!)
            outputPath = Paths.get(serviceProperties.getOrDefault("output-path", "./ids"))

            if (!Files.exists(outputPath)) {
                Files.createDirectory(outputPath)
                logger.info { "Output path does not exist. Creating directory: $outputPath" }
            } else if (!Files.isDirectory(outputPath)) {
                logger.error { "Output path specified is a file - it must be a directory!" }
            }
        }
    }

    override fun postLoad(server: Server, world: World) {
        if (!dump) {
            return
        }

        val namer = Namer()

        writeItems(namer)
        writeNpcs(namer)
        writeObjs(namer)
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun writeItems(namer: Namer) {
        val count = itemCount()
        val items = generateWriter("Items.kt")
        for (i in 0 until count) {
            val item = item(i)
            /*
             * Skip placeholder items.
             */
            if (item.isPlaceholder) {
                continue
            }
            val rawName = if (item.noteTemplateId > 0) item(item.noteLinkId).name + "_NOTED" else item.name
            if (rawName.isNotBlank()) {
                val name = namer.name(rawName, i)
                write(items, "const val $name = $i")
            }
        }
        endWriter(items)
    }

    private fun writeNpcs(namer: Namer) {
        val count = npcCount()
        val npcs = generateWriter("Npcs.kt")
        for (i in 0 until count) {
            val npc = npc(i)
            val rawName = npc.name.replace("?", "")
            if (rawName.isNotEmpty() && rawName.isNotBlank()) {
                val name = namer.name(npc.name, i)
                write(npcs, "const val $name = $i")
            }
        }
        endWriter(npcs)
    }

    private fun writeObjs(namer: Namer) {
        val count = objectCount()
        val objs = generateWriter("Objs.kt")
        for (i in 0 until count) {
            val npc = objects(i) ?: continue
            val rawName = npc.name.replace("?", "")
            if (rawName.isNotEmpty() && rawName.isNotBlank()) {
                val name = namer.name(npc.name, i)
                write(objs, "const val $name = $i")
            }
        }
        endWriter(objs)
    }

    private fun generateWriter(file: String): PrintWriter {
        val writer = PrintWriter(outputPath!!.resolve(file).toFile())
        writer.println("/* Auto-generated file using ${this::class.java} */")
        writer.println("package gg.rsmod.plugins.api.cfg")
        writer.println("")
        writer.println("object ${file.removeSuffix(".kt")} {")
        writer.println("")
        return writer
    }

    private fun write(writer: PrintWriter, text: String) {
        writer.println("    $text")
    }

    private fun endWriter(writer: PrintWriter) {
        writer.println("    /* Auto-generated file using ${this::class.java} */")
        writer.println("}")
        writer.close()
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
