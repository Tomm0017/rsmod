package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.Chunk

/**
 * A system responsible for allocating and de-allocating [InstancedMap]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMapAllocator {

    /**
     * A list of active [InstancedMap]s.
     */
    private val maps = mutableListOf<InstancedMap>()

    /**
     * The current cycles that keep track of how long before our allocated
     * should scan for 'inactive' [InstancedMap]s.
     */
    private var deallocationScanCycle = 0

    /**
     * Allocate a new [InstancedMap] given [chunks].
     *
     * @param world
     * The [World] that the instanced map is apart of.
     *
     * @param chunks
     * The [InstancedChunkSet] that holds all the [InstancedChunk]s that will make
     * up the newly constructed [InstancedMap], if applicable.
     */
    fun allocate(world: World, chunks: InstancedChunkSet, configs: InstancedMapConfiguration): InstancedMap? {
        val area = VALID_AREA
        val step = 64

        /*
         * The total amount of tiles that the new [InstancedMap] will occupy.
         */
        val totalTiles = chunks.regionSize * Chunk.REGION_SIZE

        for (x in area.bottomLeftX until area.topRightX step step) {
            for (z in area.bottomLeftZ until area.topRightZ step step) {

                /*
                 * If a map is already allocated in [x,z], we move on.
                 */
                if (maps.any { it.area.contains(x, z) || it.area.contains(x + totalTiles - 1, z + totalTiles - 1) }) {
                    continue
                }

                val map = allocate(x, z, chunks, configs)
                applyCollision(world, map, configs.bypassObjectChunkBounds)
                maps.add(map)
                return map
            }
        }

        return null
    }

    private fun allocate(x: Int, z: Int, chunks: InstancedChunkSet, configs: InstancedMapConfiguration): InstancedMap =
            InstancedMap(Area(x, z, x + chunks.regionSize * Chunk.REGION_SIZE, z + chunks.regionSize * Chunk.REGION_SIZE), chunks,
                    configs.exitTile, configs.owner, configs.attributes)

    private fun deallocate(world: World, map: InstancedMap) {
        if (maps.remove(map)) {
            removeCollision(world, map)
            world.removeAll(map.area)

            /**
             * If the map is de-allocated, we want to move any players in the
             * instance to the [InstancedMap.exitTile].
             */
            world.players.forEach { player ->
                if (map.area.contains(player.tile)) {
                    player.moveTo(map.exitTile)
                }
            }
        }
    }

    internal val activeMapCount: Int get() = maps.size

    internal fun logout(player: Player) {
        val world = player.world

        getMap(player.tile)?.let { map ->
            player.moveTo(map.exitTile)

            if (map.attr.contains(InstancedMapAttribute.DEALLOCATE_ON_LOGOUT)) {
                val mapOwner = map.owner!! // If map has this attribute, they should also set an owner.
                if (player.uid == mapOwner) {
                    deallocate(world, map)
                }
            }
        }
    }

    internal fun death(player: Player) {
        val world = player.world

        getMap(player.tile)?.let { map ->
            if (map.attr.contains(InstancedMapAttribute.DEALLOCATE_ON_DEATH)) {
                val mapOwner = map.owner!! // If map has this attribute, they should also set an owner.
                if (player.uid == mapOwner) {
                    deallocate(world, map)
                }
            }
        }
    }

    internal fun cycle(world: World) {
        if (deallocationScanCycle++ == SCAN_MAPS_CYCLES) {

            for (i in 0 until maps.size) {
                val map = maps[i]

                /*
                 * If there's no players in the [map] area, we can de-allocate
                 * the map.
                 */
                if (world.players.none { map.area.contains(it.tile) }) {
                    deallocate(world, map)
                }
            }

            deallocationScanCycle = 0
        }
    }

    /**
     * @return
     * An [InstancedMap] who's area contains [tile], or null if no map is found in said tile.
     */
    fun getMap(tile: Tile): InstancedMap? = maps.find { it.area.contains(tile) }

    private fun applyCollision(world: World, map: InstancedMap, bypassObjectChunkBounds: Boolean) {
        val bounds = Chunk.CHUNKS_PER_REGION * map.chunks.regionSize
        val heights = Tile.TOTAL_HEIGHT_LEVELS

        val chunks = map.chunks.values

        for (height in 0 until heights) {
            for (x in 0 until bounds) {
                for (z in 0 until bounds) {
                    val coords = InstancedChunkSet.getCoordinates(x, z, height)
                    val chunk = chunks[coords]

                    val chunkH = (coords shr 28) and 0x3
                    val chunkX = (coords shr 14) and 0x3FF
                    val chunkZ = coords and 0x7FF

                    val baseTile = map.area.bottomLeft.transform(chunkX shl 3, chunkZ shl 3, chunkH)
                    val newChunk = world.chunks.getOrCreate(baseTile)

                    if (chunk != null) {
                        val copyTile = Tile.fromRotatedHash(chunk.packed)
                        val copyChunk = world.chunks.get(copyTile.chunkCoords, createIfNeeded = true)!!

                        copyChunk.getEntities<StaticObject>(EntityType.STATIC_OBJECT).forEach { obj ->
                            if (obj.tile.height == chunkH && obj.tile.isInSameChunk(copyTile)) {
                                val def = obj.getDef(world.definitions)
                                val width = def.getRotatedWidth(obj)
                                val length = def.getRotatedLength(obj)

                                val localX = obj.tile.x % 8
                                val localZ = obj.tile.z % 8

                                val newObj = DynamicObject(obj.id, obj.type, (obj.rot + chunk.rot) and 0x3, baseTile.transformAndRotate(localX, localZ, chunk.rot, width, length))
                                val insideChunk = newObj.tile.isInSameChunk(baseTile)

                                if (insideChunk) {
                                    newChunk.addEntity(world, newObj, newObj.tile)
                                } else if (!bypassObjectChunkBounds) {
                                    throw IllegalStateException("Could not copy object due to its size and rotation outcome (object rotation + chunk rotation). " +
                                            "The object would, otherwise, be spawned out of bounds of its original chunk. [obj=$obj, copy=$newObj]")
                                }
                            }
                        }

                        copyChunk.blockedTiles.forEach { tile ->
                            if (tile.height == chunkH && tile.isInSameChunk(copyTile)) {
                                val localX = tile.x % 8
                                val localZ = tile.z % 8
                                val local = baseTile.transformAndRotate(localX, localZ, chunk.rot)
                                newChunk.getMatrix(chunkH).block(local.x % 8, local.z % 8, impenetrable = true)
                            }
                        }
                    } else {
                        for (lx in 0 until Chunk.CHUNK_SIZE) {
                            for (lz in 0 until Chunk.CHUNK_SIZE) {
                                newChunk.getMatrix(chunkH).block(lx, lz, impenetrable = true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun removeCollision(world: World, map: InstancedMap) {
        val regionCount = map.chunks.regionSize
        val chunks = world.chunks

        for (i in 0 until regionCount) {
            val tile = map.area.bottomLeft.transform(i * Chunk.REGION_SIZE, i * Chunk.REGION_SIZE)
            chunks.remove(tile.chunkCoords)
        }
    }

    companion object {
        /**
         * 07 identifies instanced maps by having an X-axis of 6400 or above. They
         * use this for some client scripts, such as a Theatre of Blood cs2 that
         * will change depending on if you're inside the Theatre (in an instance)
         * or in the lobby.
         *
         * Current stats (as of 17/03/2018):
         * Area size: 3200x6400
         * Instance support: 5000 [50 in x-axis * 100 in z-axis]
         *
         * [Instance support]: the amount of instances the world can support at a time,
         * assuming every map is 64x64 in size, which isn't always the case.
         */
        private val VALID_AREA = Area(bottomLeftX = 6400, bottomLeftZ = 0, topRightX = 9600, topRightZ = 6400)

        /**
         * The amount of game cycles that must go by before scanning the active
         * [maps] for any [InstancedMap] eligible to be de-allocated.
         */
        private const val SCAN_MAPS_CYCLES = 25
    }
}