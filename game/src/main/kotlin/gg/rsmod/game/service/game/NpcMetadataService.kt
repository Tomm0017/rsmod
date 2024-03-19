package gg.rsmod.game.service.game

import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcMetadataService : Service {

    private lateinit var path: Path
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {

        path = Paths.get(serviceProperties.getOrDefault("path", "../data/cfg/npcs.csv"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load(world.definitions)
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load(definitions: DefinitionSet) {
        path.toFile().forEachLine { line ->
            val parts = line.split(',')
            if (parts.size >= 2) {
                val id = parts[0].toIntOrNull()
                val examine = line.substringAfter(',').trim()
                if (id != null) {
                    val def = definitions.getNullable(NpcDef::class.java, id) ?: return@forEachLine
                    def.examine = examine.replace("\"", "")
                }
            }
        }
    }
}