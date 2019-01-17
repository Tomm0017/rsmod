package gg.rsmod.game.service.game.item

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.model.item.ItemStats
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import org.apache.logging.log4j.LogManager
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemStatsService : Service() {

    companion object {
        private val logger = LogManager.getLogger(ItemStatsService::class.java)
    }

    private val items = hashMapOf<Int, ItemStats>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/item-stats.json"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load(path)
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    fun get(item: Int): ItemStats? = items[item]

    private fun load(path: Path) {
        Files.newBufferedReader(path).use { reader ->
            val parsed = Gson().fromJson<List<ItemStats>>(reader, object : TypeToken<List<ItemStats>>() {}.type)
            parsed.forEach { stats ->
                items[stats.item] = stats
            }
        }
        logger.info("Loaded {} item stats.", items.size)
    }
}