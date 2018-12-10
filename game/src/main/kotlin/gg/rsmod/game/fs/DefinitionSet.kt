package gg.rsmod.game.fs

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.service.xtea.XteaKeyService
import gg.rsmod.util.NamedThreadFactory
import net.runelite.cache.ConfigType
import net.runelite.cache.IndexType
import net.runelite.cache.definitions.EnumDefinition
import net.runelite.cache.definitions.NpcDefinition
import net.runelite.cache.definitions.ObjectDefinition
import net.runelite.cache.definitions.VarbitDefinition
import net.runelite.cache.definitions.loaders.*
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Holds all definitions that we need from our [Store].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionSet {

    companion object {
        private val logger = LogManager.getLogger(DefinitionSet::class.java)
    }

    /**
     * A [HashMap] holding all definitions with their [Class] as key.
     */
    private val defs = hashMapOf<Class<*>, Array<Any>>()

    private val regionDecoderExecutor = Executors.newSingleThreadExecutor(NamedThreadFactory().setName("region-decoder-thread").build())

    @Suppress("UNCHECKED_CAST")
    @Throws(RuntimeException::class)
    fun init(store: Store) {
        /**
         * Load [IndexType.CONFIGS] definitions.
         */
        val configs = store.getIndex(IndexType.CONFIGS)!!

        /**
         * Load [Varp]s.
         */
        val varpArchive = configs.getArchive(ConfigType.VARPLAYER.id)!!
        val varpFiles = varpArchive.getFiles(store.storage.loadArchive(varpArchive)!!).files
        val varps = arrayListOf<VarpDefinition>()
        repeat(varpFiles.size) {
            varps.add(VarpDefinition())
        }
        defs[VarpDefinition::class.java] = varps.toTypedArray() as Array<Any>

        logger.info("Loaded ${varps.size} varp definitions.")

        /**
         * Load [VarbitDef]s.
         */
        val varbitArchive = configs.getArchive(ConfigType.VARBIT.id)!!
        val varbitFiles = varbitArchive.getFiles(store.storage.loadArchive(varbitArchive)!!).files
        val varbits = arrayListOf<VarbitDefinition>()
        for (file in varbitFiles) {
            val loader = VarbitLoader()
            val def = loader.load(file.fileId, file.contents)
            varbits.add(def)
        }
        defs[VarbitDefinition::class.java] = varbits.toTypedArray() as Array<Any>

        logger.info("Loaded ${varbits.size} varbit definitions.")

        /**
         * Load [EnumDefinition]s.
         */
        val enumArchive = configs.getArchive(ConfigType.ENUM.id)!!
        val enumFiles = enumArchive.getFiles(store.storage.loadArchive(enumArchive)!!).files
        val enums = arrayListOf<EnumDefinition>()
        for (file in enumFiles) {
            val loader = EnumLoader()
            val def = loader.load(file.fileId, file.contents)
            enums.add(def)
        }
        defs[EnumDefinition::class.java] = enums.toTypedArray() as Array<Any>

        logger.info("Loaded ${enums.size} enum definitions.")

        /**
         * Load [NpcDefinition]s.
         */
        val npcArchive = configs.getArchive(ConfigType.NPC.id)!!
        val npcFiles = npcArchive.getFiles(store.storage.loadArchive(npcArchive)!!).files
        val npcs = arrayListOf<NpcDefinition>()
        for (file in npcFiles) {
            val loader = NpcLoader()
            val def = loader.load(file.fileId, file.contents)
            npcs.add(def)
        }
        defs[NpcDefinition::class.java] = npcs.toTypedArray() as Array<Any>

        logger.info("Loaded ${npcs.size} npc definitions.")

        /**
         * Load [ObjectDefinition]s.
         */
        val objArchive = configs.getArchive(ConfigType.OBJECT.id)!!
        val objFiles = objArchive.getFiles(store.storage.loadArchive(objArchive)!!).files
        val objs = arrayListOf<ObjectDefinition>()
        for (file in objFiles) {
            val loader = ObjectLoader()
            val def = loader.load(file.fileId, file.contents)
            objs.add(def)
        }
        defs[ObjectDefinition::class.java] = objs.toTypedArray() as Array<Any>

        logger.info("Loaded ${objs.size} object definitions.")
    }

    fun getCount(type: Class<*>) = defs[type]!!.size

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(type: Class<T>): Array<T> {
        return defs[type]!! as Array<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(type: Class<T>, id: Int): T {
        return (defs[type]!!)[id] as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrNull(type: Class<T>, id: Int): T? {
        return (defs[type]!!)[id] as T?
    }

    fun createRegion(world: World, id: Int): Boolean {
        val xteaService = world.getService(XteaKeyService::class.java, true).orElse(null)
        val regionIndex = world.filestore.getIndex(IndexType.MAPS)

        val x = id shr 8
        val z = id and 0xFF

        val mapArchive = regionIndex.findArchiveByName("m${x}_$z") ?: return false
        val landArchive = regionIndex.findArchiveByName("l${x}_$z") ?: return false

        val mapData = mapArchive.decompress(world.filestore.storage.loadArchive(mapArchive))
        val mapDefinition = MapLoader().load(x, z, mapData)

        val cacheRegion = net.runelite.cache.region.Region(id)
        cacheRegion.loadTerrain(mapDefinition)

        for (height in 0 until 4) {
            for (lx in 0 until 64) {
                for (lz in 0 until 64) {
                    val tileSetting = cacheRegion.getTileSetting(height, lx, lz)
                    val tile = Tile(cacheRegion.baseX + lx, cacheRegion.baseY + lz, height)

                    if ((tileSetting.toInt() and CollisionManager.BLOCKED_TILE) == CollisionManager.BLOCKED_TILE) {
                        world.collision.block(tile)
                    }

                    if ((tileSetting.toInt() and CollisionManager.BRIDGE_TILE) == CollisionManager.BRIDGE_TILE) {
                        world.collision.markBridged(tile)
                    }
                }
            }
        }

        if (xteaService != null) {
            val keys = xteaService.getOrNull(id) ?: return false
            try {
                val landData = landArchive.decompress(world.filestore.storage.loadArchive(landArchive), keys)
                val locDef = LocationsLoader().load(x, z, landData)
                cacheRegion.loadLocations(locDef)

                cacheRegion.locations.forEach { loc ->
                    val tile = Tile(loc.position.x, loc.position.y, loc.position.z)
                    val obj = StaticObject(loc.id, loc.type, loc.orientation, tile)
                    world.collision.regions.getChunkForTile(world, tile).addEntity(world, obj)
                }
                return true
            } catch (e: IOException) {
                logger.error("Could not decrypt map region {}.", id)
                return false
            }
        }

        return true
    }
}