package gg.rsmod.game.service.game

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import dev.openrune.cache.CacheManager.objects
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectMetadataService : Service {

    private lateinit var path: Path

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.getOrDefault("path", "../data/cfg/locs.csv"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load()
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load() {
        path.toFile().forEachLine { line ->
            val parts = line.split(',')
            if (parts.size >= 2) {
                val id = parts[0].toIntOrNull()
                val examine = line.substringAfter(',').trim()
                if (id != null) {
                    val def = objects(id) ?: return@forEachLine
                    def.examine = examine.replace("\"", "")
                }
            }
        }
    }

    private data class Metadata(val id: Int = -1, val examine: String? = null)
}