package gg.rsmod.game.service.xtea

import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import org.apache.commons.io.FilenameUtils
import org.apache.logging.log4j.LogManager
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * A [Service] that loads and exposes XTEA keys required for map decryption.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class XteaKeyService : Service() {

    companion object {
        private val logger = LogManager.getLogger(XteaKeyService::class.java)
    }

    private val keys = hashMapOf<Int, IntArray>()

    @Throws(Exception::class)
    override fun init(server: Server, gameContext: GameContext, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.get<String>("path")!!)
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        Files.list(path).forEach { list ->
            val region = FilenameUtils.removeExtension(list.fileName.toString()).toInt()
            val keys = IntArray(4)
            Files.newBufferedReader(list).useLines { lines ->
                lines.forEachIndexed { index, line ->
                    val key = line.toInt()
                    keys[index] = key
                }
            }
            this.keys[region] = keys
        }
        logger.info("Loaded {} XTEA keys.", keys.size)
    }

    override fun terminate(server: Server, gameContext: GameContext) {
    }

    fun get(region: Int): IntArray {
        if (keys[region] == null) {
            logger.warn("No XTEA keys found for region {}.", region)
            keys[region] = IntArray(4)
        }
        return keys[region]!!
    }

}