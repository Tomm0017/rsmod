package gg.rsmod.game.service.game

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcMetadataService : Service {

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/npcs.yml"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }

        Files.newBufferedReader(path).use { reader ->
            load(world.definitions, reader)
        }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load(definitions: DefinitionSet, reader: BufferedReader) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.propertyNamingStrategy = PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES

        mapper.readValue(reader, Array<Metadata?>::class.java)?.let { metadataSet ->
            metadataSet.filterNotNull().forEach { metadata ->
                val def = definitions.get(NpcDef::class.java, metadata.id)
                def.examine = metadata.examine
            }
        }
    }

    private data class Metadata(val id: Int = -1, val examine: String? = null)
}