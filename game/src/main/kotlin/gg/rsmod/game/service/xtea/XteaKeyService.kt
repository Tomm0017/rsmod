package gg.rsmod.game.service.xtea

import com.google.gson.Gson
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KotlinLogging
import net.runelite.cache.IndexType
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
        private val logger = KotlinLogging.logger {  }
    }

    private val keys = hashMapOf<Int, IntArray>()

    @Throws(Exception::class)
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.get<String>("path")!!)
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        if (Files.exists(path.resolve("xteas.json"))) {
            val reader = Files.newBufferedReader(path.resolve("xteas.json"))
            val xteas = Gson().fromJson(reader, Array<XteaFile>::class.java)
            reader.close()
            xteas?.forEach { xtea ->
                keys[xtea.region] = xtea.keys
            }
        } else {
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
        }

        /**
         * Get the total amount of valid regions and which keys we are missing.
         */
        val maxRegions = Short.MAX_VALUE
        var totalRegions = 0
        val missingKeys = arrayListOf<Int>()

        val regionIndex = world.filestore.getIndex(IndexType.MAPS)
        for (regionId in 0 until maxRegions) {
            val x = regionId shr 8
            val z = regionId and 0xFF

            /**
             * Check if the region corresponding to the [x] and [z] can be
             * found in our cache.
             */
            regionIndex.findArchiveByName("m${x}_$z") ?: continue
            regionIndex.findArchiveByName("l${x}_$z") ?: continue

            /**
             * The region was found in the [regionIndex].
             */
            totalRegions++

            /**
             * If the XTEA is not found in our [xteaService], we know the keys
             * are missing.
             */
            if (getOrNull(regionId) == null) {
                missingKeys.add(regionId)
            }
        }

        /**
         * Set the XTEA service for the [World].
         */
        world.xteaKeyService = this

        val validKeys = totalRegions - missingKeys.size
        logger.info("Loaded {} / {} ({}%) XTEA keys.", validKeys, totalRegions,
                String.format("%.2f", (validKeys.toDouble() * 100.0) / totalRegions.toDouble()))

    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    fun get(region: Int): IntArray {
        if (keys[region] == null) {
            logger.warn("No XTEA keys found for region {}.", region)
            keys[region] = IntArray(4)
        }
        return keys[region]!!
    }

    fun getOrNull(region: Int): IntArray? = keys[region]

    private data class XteaFile(val region: Int, val keys: IntArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as XteaFile

            if (region != other.region) return false
            if (!keys.contentEquals(other.keys)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = region
            result = 31 * result + keys.contentHashCode()
            return result
        }
    }
}