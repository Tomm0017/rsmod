package gg.rsmod.game.fs

import io.github.oshai.kotlinlogging.KotlinLogging
import gg.rsmod.game.fs.def.*
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.ChunkSet
import gg.rsmod.game.service.xtea.XteaKeyService
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.runelite.cache.ConfigType
import net.runelite.cache.IndexType
import net.runelite.cache.definitions.loaders.LocationsLoader
import net.runelite.cache.definitions.loaders.MapLoader
import net.runelite.cache.fs.Store
import java.io.FileNotFoundException
import java.io.IOException

/**
 * A [DefinitionSet] is responsible for loading any relevant metadata found in
 * the game resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionSet {

    /**
     * A [Map] holding all definitions with their [Class] as key.
     */
    private val defs = Object2ObjectOpenHashMap<Class<out Definition>, Map<Int, *>>()

    private var xteaService: XteaKeyService? = null

    fun loadAll(store: Store) {
        /*
         * Load [AnimDef]s.
         */
        load(store, AnimDef::class.java)
        logger.info("Loaded ${getCount(AnimDef::class.java)} animation definitions.")

        /*
         * Load [VarpDef]s.
         */
        load(store, VarpDef::class.java)
        logger.info("Loaded ${getCount(VarpDef::class.java)} varp definitions.")

        /*
         * Load [VarbitDef]s.
         */
        load(store, VarbitDef::class.java)
        logger.info("Loaded ${getCount(VarbitDef::class.java)} varbit definitions.")

        /*
         * Load [EnumDef]s.
         */
        load(store, EnumDef::class.java)
        logger.info("Loaded ${getCount(EnumDef::class.java)} enum definitions.")

        /*
         * Load [NpcDef]s.
         */
        load(store, NpcDef::class.java)
        logger.info("Loaded ${getCount(NpcDef::class.java)} npc definitions.")

        /*
         * Load [ItemDef]s.
         */
        load(store, ItemDef::class.java)
        logger.info("Loaded ${getCount(ItemDef::class.java)} item definitions.")

        /*
         * Load [ObjectDef]s.
         */
        load(store, ObjectDef::class.java)
        logger.info("Loaded ${getCount(ObjectDef::class.java)} object definitions.")
    }

    fun loadRegions(world: World, chunks: ChunkSet, regions: IntArray) {
        val start = System.currentTimeMillis()

        var loaded = 0
        regions.forEach { region ->
            if (chunks.activeRegions.add(region)) {
                if (createRegion(world, region)) {
                    loaded++
                }
            }
        }
        logger.info { "Loaded $loaded regions in ${System.currentTimeMillis() - start}ms" }
    }

    fun <T : Definition> load(store: Store, type: Class<out T>) {
        val configType: ConfigType = when (type) {
            VarpDef::class.java -> ConfigType.VARPLAYER
            VarbitDef::class.java -> ConfigType.VARBIT
            EnumDef::class.java -> ConfigType.ENUM
            NpcDef::class.java -> ConfigType.NPC
            ObjectDef::class.java -> ConfigType.OBJECT
            ItemDef::class.java -> ConfigType.ITEM
            AnimDef::class.java -> ConfigType.SEQUENCE
            else -> throw IllegalArgumentException("Unhandled class type ${type::class.java}.")
        }
        val configs = store.getIndex(IndexType.CONFIGS) ?: throw FileNotFoundException("Cache was not found.")
        val archive = configs.getArchive(configType.id)!!
        val files = archive.getFiles(store.storage.loadArchive(archive)!!).files

        val definitions = Int2ObjectOpenHashMap<T?>(files.size + 1)
        for (i in 0 until files.size) {

                val def = createDefinition(type, files[i].fileId, files[i].contents)
                definitions[files[i].fileId] = def

        }
        defs[type] = definitions
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> createDefinition(type: Class<out T>, id: Int, data: ByteArray): T {
        val def: Definition = when (type) {
            VarpDef::class.java -> VarpDef(id)
            VarbitDef::class.java -> VarbitDef(id)
            EnumDef::class.java -> EnumDef(id)
            NpcDef::class.java -> NpcDef(id)
            ObjectDef::class.java -> ObjectDef(id)
            ItemDef::class.java -> ItemDef(id)
            AnimDef::class.java -> AnimDef(id)
            else -> throw IllegalArgumentException("Unhandled class type ${type::class.java}.")
        }

        val buf = Unpooled.wrappedBuffer(data)
        def.decode(buf)
        buf.release()
        return def as T
    }

    fun getCount(type: Class<*>) = defs[type]!!.size

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> get(type: Class<out T>, id: Int): T {
        return (defs[type]!!)[id] as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Definition> getNullable(type: Class<out T>, id: Int): T? {
        return (defs[type]!!)[id] as T?
    }

    /**
     * Creates an 8x8 [gg.rsmod.game.model.region.Chunk] region.
     */
    fun createRegion(world: World, id: Int): Boolean {
        if (xteaService == null) {
            xteaService = world.getService(XteaKeyService::class.java)
        }

        val regionIndex = world.filestore.getIndex(IndexType.MAPS)

        val x = id shr 8
        val z = id and 0xFF

        val mapArchive = regionIndex.findArchiveByName("m${x}_$z") ?: return false
        val landArchive = regionIndex.findArchiveByName("l${x}_$z") ?: return false

        val mapData = mapArchive.decompress(world.filestore.storage.loadArchive(mapArchive))
        if (mapData == null) {
            logger.error { "Map data null for region $id ($x, $z)" }
            return false
        }
        val mapDefinition = MapLoader().load(x, z, mapData)

        val cacheRegion = net.runelite.cache.region.Region(id)
        cacheRegion.loadTerrain(mapDefinition)

        val blocked = hashSetOf<Tile>()
        val bridges = hashSetOf<Tile>()

        for (height in 0 until 3) {
            for (lx in 0 until 63) {
                for (lz in 0 until 63) {
                    val bridge = cacheRegion.getTileSetting(1, lx, lz).toInt() and 0x2 != 0
                    if (bridge) {
                        bridges.add(Tile(cacheRegion.baseX + lx, cacheRegion.baseY + lz, height))
                    }
                    val blockedTile = cacheRegion.getTileSetting(height, lx, lz).toInt() and 0x1 != 0
                    if (blockedTile) {
                        val level = if (bridge) (height - 1) else height
                        if (level < 0) continue
                        blocked.add(Tile(cacheRegion.baseX + lx, cacheRegion.baseY + lz, level))
                    }
                }
            }
        }

        /*
         * Apply the blocked tiles to the collision detection.
         */
        val blockedTileBuilder = CollisionUpdate.Builder()
        blockedTileBuilder.setType(CollisionUpdate.Type.ADD)
        blocked.forEach { tile ->
            world.chunks.getOrCreate(tile).blockedTiles.add(tile)
            blockedTileBuilder.putTile(tile, false, *Direction.NESW)
        }
        world.collision.applyUpdate(blockedTileBuilder.build())

        if (xteaService == null) {
            /*
             * If we don't have an [XteaKeyService], then we assume we don't
             * need to decrypt the files through xteas. This means the objects
             * from each region has to be decrypted a different way.
             *
             * If this is the case, you need to use [gg.rsmod.game.model.region.Chunk.addEntity]
             * to add the object to the world for collision detection.
             */
            return true
        }

        val keys = xteaService?.get(id) ?: XteaKeyService.EMPTY_KEYS
        try {
            val landData = landArchive.decompress(world.filestore.storage.loadArchive(landArchive), keys)
            val locDef = LocationsLoader().load(x, z, landData)
            cacheRegion.loadLocations(locDef)
            cacheRegion.locations.forEach { loc ->
                val tile = Tile(loc.position.x, loc.position.y, loc.position.z)
                val hasBridge = bridges.contains(tile)
                if (hasBridge && loc.position.z == 0) return@forEach
                val adjustedTile = if (bridges.contains(tile)) tile.transform(-1) else tile
                val obj = StaticObject(loc.id, loc.type, loc.orientation, adjustedTile)
                world.chunks.getOrCreate(adjustedTile).addEntity(world, obj, adjustedTile)
            }
            return true
        } catch (e: IOException) {
            logger.error("Could not decrypt map region {}.", id)
            return false
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}