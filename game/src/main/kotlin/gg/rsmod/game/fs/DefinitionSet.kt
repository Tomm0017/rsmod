package gg.rsmod.game.fs

import gg.rsmod.game.fs.def.*
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.service.xtea.XteaKeyService
import io.netty.buffer.Unpooled
import net.runelite.cache.ConfigType
import net.runelite.cache.IndexType
import net.runelite.cache.definitions.loaders.LocationsLoader
import net.runelite.cache.definitions.loaders.MapLoader
import net.runelite.cache.fs.FSFile
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager
import java.io.IOException

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
    private val defs = hashMapOf<Class<out Definition>, Array<*>>()

    private var xteaService: XteaKeyService? = null

    @Suppress("UNCHECKED_CAST")
    @Throws(RuntimeException::class)
    fun init(store: Store) {
        /**
         * Load [IndexType.CONFIGS] definitions.
         */
        val configs = store.getIndex(IndexType.CONFIGS)!!

        /**
         * Load [VarpDef]s.
         */
        val varpArchive = configs.getArchive(ConfigType.VARPLAYER.id)!!
        val varpFiles = varpArchive.getFiles(store.storage.loadArchive(varpArchive)!!).files
        loadDefinitions(VarpDef::class.java, varpFiles)
        logger.info("Loaded ${varpFiles.size} varp definitions.")

        /**
         * Load [VarbitDef]s.
         */
        val varbitArchive = configs.getArchive(ConfigType.VARBIT.id)!!
        val varbitFiles = varbitArchive.getFiles(store.storage.loadArchive(varbitArchive)!!).files
        loadDefinitions(VarbitDef::class.java, varbitFiles)
        logger.info("Loaded ${varbitFiles.size} varbit definitions.")

        /**
         * Load [EnumDef]s.
         */
        val enumArchive = configs.getArchive(ConfigType.ENUM.id)!!
        val enumFiles = enumArchive.getFiles(store.storage.loadArchive(enumArchive)!!).files
        loadDefinitions(EnumDef::class.java, enumFiles)
        logger.info("Loaded ${enumFiles.size} enum definitions.")

        /**
         * Load [NpcDef]s.
         */
        val npcArchive = configs.getArchive(ConfigType.NPC.id)!!
        val npcFiles = npcArchive.getFiles(store.storage.loadArchive(npcArchive)!!).files
        loadDefinitions(NpcDef::class.java, npcFiles)
        logger.info("Loaded ${npcFiles.size} npc definitions.")

        /**
         * Load [ItemDef]s.
         */
        val itemArchive = configs.getArchive(ConfigType.ITEM.id)!!
        val itemFiles = itemArchive.getFiles(store.storage.loadArchive(itemArchive)!!).files
        loadDefinitions(ItemDef::class.java, itemFiles)
        logger.info("Loaded ${itemFiles.size} item definitions.")

        /**
         * Load [ObjectDef]s.
         */
        val objArchive = configs.getArchive(ConfigType.OBJECT.id)!!
        val objFiles = objArchive.getFiles(store.storage.loadArchive(objArchive)!!).files
        loadDefinitions(ObjectDef::class.java, objFiles)
        logger.info("Loaded ${objFiles.size} object definitions.")
    }

    private inline fun <reified T: Definition> loadDefinitions(type: Class<T>, files: MutableList<FSFile>) {
        val definitions = Array<T?>(files.size + 1) { null }
        for (i in 0 until files.size) {
            val def = createDefinition(type, files[i].fileId, files[i].contents)
            definitions[files[i].fileId] = def
        }
        defs[type] = definitions
    }

    fun getCount(type: Class<*>) = defs[type]!!.size

    @Suppress("UNCHECKED_CAST")
    operator fun <T: Definition> get(type: Class<out T>): Array<T> {
        return defs[type]!! as Array<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Definition> get(type: Class<out T>, id: Int): T {
        return (defs[type]!!)[id] as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Definition> createDefinition(type: Class<out T>, id: Int, data: ByteArray): T {
        val def: Definition = when (type) {
            VarpDef::class.java -> VarpDef(id)
            VarbitDef::class.java -> VarbitDef(id)
            EnumDef::class.java -> EnumDef(id)
            NpcDef::class.java -> NpcDef(id)
            ObjectDef::class.java -> ObjectDef(id)
            ItemDef::class.java -> ItemDef(id)
            else -> throw IllegalArgumentException("Unhandled definition class type ${type::class.java}.")
        }

        val buf = Unpooled.wrappedBuffer(data)
        def.decode(buf)
        buf.release()
        return def as T
    }

    fun createRegion(world: World, id: Int): Boolean {
        if (xteaService == null) {
            xteaService = world.getService(XteaKeyService::class.java, true).orElse(null)
        }

        val regionIndex = world.filestore.getIndex(IndexType.MAPS)

        val x = id shr 8
        val z = id and 0xFF

        val mapArchive = regionIndex.findArchiveByName("m${x}_$z") ?: return false
        val landArchive = regionIndex.findArchiveByName("l${x}_$z") ?: return false

        val mapData = mapArchive.decompress(world.filestore.storage.loadArchive(mapArchive))
        val mapDefinition = MapLoader().load(x, z, mapData)

        val cacheRegion = net.runelite.cache.region.Region(id)
        cacheRegion.loadTerrain(mapDefinition)

        val blocked = hashSetOf<Tile>()
        val bridges = hashSetOf<Tile>()
        for (height in 0 until 4) {
            for (lx in 0 until 64) {
                for (lz in 0 until 64) {
                    val tileSetting = cacheRegion.getTileSetting(height, lx, lz)
                    val tile = Tile(cacheRegion.baseX + lx, cacheRegion.baseY + lz, height)

                    if ((tileSetting.toInt() and CollisionManager.BLOCKED_TILE) == CollisionManager.BLOCKED_TILE) {
                        blocked.add(tile)
                    }

                    if ((tileSetting.toInt() and CollisionManager.BRIDGE_TILE) == CollisionManager.BRIDGE_TILE) {
                        bridges.add(tile)
                        /**
                         * We don't want the bottom of the bridge to be blocked,
                         * so remove the blocked tile if applicable.
                         */
                        blocked.remove(tile.transform(-1))
                    }
                }
            }
        }

        /**
         * Apply the blocked tiles to the collision detection.
         */
        val blockedTileBuilder = CollisionUpdate.Builder()
        blockedTileBuilder.setType(CollisionUpdate.Type.ADDING)
        blocked.forEach { tile ->
            blockedTileBuilder.putTile(tile, false, *Direction.NESW)
        }
        world.collision.apply(blockedTileBuilder.build())

        if (xteaService == null) {
            /**
             * If we don't have an [XteaKeyService], then we assume we don't
             * need to decrypt the files through xteas. This means the objects
             * from each region has to be decrypted a different way.
             *
             * If this is the case, you need to use [gg.rsmod.game.model.region.Chunk.addEntity]
             * to add the object to the world for collision detection.
             */
            return true
        }

        val keys = xteaService?.getOrNull(id) ?: return false
        try {
            val landData = landArchive.decompress(world.filestore.storage.loadArchive(landArchive), keys)
            val locDef = LocationsLoader().load(x, z, landData)
            cacheRegion.loadLocations(locDef)

            cacheRegion.locations.forEach { loc ->
                val tile = Tile(loc.position.x, loc.position.y, loc.position.z)
                if (bridges.contains(tile.transform(1))) {
                    return@forEach
                }
                val obj = StaticObject(loc.id, loc.type, loc.orientation,
                        if (bridges.contains(tile)) tile.transform(-1) else tile)
                world.regions.getChunkForTile(tile).addEntity(world, obj)
            }
            return true
        } catch (e: IOException) {
            logger.error("Could not decrypt map region {}.", id)
            return false
        }
    }
}