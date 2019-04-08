package gg.rsmod.game.service.game

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EntityExamineService : Service {

    private lateinit var path: Path

    private lateinit var world: World

    private val objects = Int2ObjectOpenHashMap<String>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        this.path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/examines/"))
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw FileNotFoundException("Path not found: ${path.toAbsolutePath()}")
        }
        this.world = world
        loadObjects("objects.json")
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    fun getObj(id: Int): String = objects[id] ?: "It's a ${world.definitions.get(ObjectDef::class.java, id).name}"

    private fun loadObjects(fileName: String) {
        load(fileName, objects)
    }

    private fun load(fileName: String, to: MutableMap<Int, String>) {
        Files.newBufferedReader(path.resolve(fileName)).use { reader ->
            val parsed = Gson().fromJson<List<Examine>>(reader, object : TypeToken<List<Examine>>() {}.type)
            parsed.forEach { examine ->
                to[examine.id] = examine.examine
            }
        }
    }

    private data class Examine(val id: Int, val examine: String)
}