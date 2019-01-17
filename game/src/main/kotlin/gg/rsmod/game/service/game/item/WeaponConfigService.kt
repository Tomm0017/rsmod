package gg.rsmod.game.service.game.item

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.model.item.WeaponConfig
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
class WeaponConfigService : Service() {

    companion object {
        private val logger = LogManager.getLogger(WeaponConfigService::class.java)
    }

    private val items = hashMapOf<Int, WeaponConfig>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/weapons.json"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        load(path)
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    fun get(item: Int): WeaponConfig? = items[item]

    private fun load(path: Path) {
        Files.newBufferedReader(path).use { reader ->
            val parsed = Gson().fromJson<List<WeaponConfig>>(reader, object : TypeToken<List<WeaponConfig>>() {}.type)
            parsed.forEach { weapon ->
                items[weapon.item] = weapon
            }
        }
        logger.info("Loaded {} weapon configs.", items.size)
    }
}