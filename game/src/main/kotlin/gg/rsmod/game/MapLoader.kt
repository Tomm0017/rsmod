package gg.rsmod.game

import dev.openrune.cache.CacheManager.cache
import dev.openrune.cache.util.Index
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.ChunkSet
import gg.rsmod.game.service.xtea.XteaKeyService
import io.github.oshai.kotlinlogging.KotlinLogging
import net.runelite.cache.definitions.loaders.LocationsLoader
import net.runelite.cache.definitions.loaders.MapLoader
import java.io.IOException

/**
 * A [MapLoader] is responsible for loading any relevant metadata found in
 * the game resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object MapLoader {

    private var xteaService: XteaKeyService? = null

    private val logger = KotlinLogging.logger{}

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

    /**
     * Creates an 8x8 [gg.rsmod.game.model.region.Chunk] region.
     */
    fun createRegion(world: World, id: Int): Boolean {
        if (xteaService == null) {
            xteaService = world.getService(XteaKeyService::class.java)
        }

        val x = id shr 8
        val z = id and 0xFF

        val mapData = cache.data(Index.MAPS,"m${x}_${z}")

        if (mapData == null) {
            logger.error { "Map data null for region $id ($x, $z)" }
            return false
        }
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
                        /*
                         * We don't want the bottom of the bridge to be blocked,
                         * so remove the blocked tile if applicable.
                         */
                        blocked.remove(tile.transform(-1))
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
            val landData = cache.data(Index.MAPS ,"l${x}_$z",keys)
            val locDef = LocationsLoader().load(x, z, landData)
            cacheRegion.loadLocations(locDef)

            cacheRegion.locations.forEach { loc ->
                val tile = Tile(loc.position.x, loc.position.y, loc.position.z)
                if (bridges.contains(tile.transform(1))) {
                    return@forEach
                }
                val obj = StaticObject(loc.id, loc.type, loc.orientation, if (bridges.contains(tile)) tile.transform(-1) else tile)
                world.chunks.getOrCreate(tile).addEntity(world, obj, obj.tile)
            }
            return true
        } catch (e: IOException) {
            logger.error { "${"Could not decrypt map region {}."} $id" }
            return false
        }
    }

}