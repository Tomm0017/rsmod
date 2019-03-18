package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.Chunk
import mu.KLogging

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMapAllocator {

    companion object : KLogging() {

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
    }

    private val maps = arrayListOf<InstancedMap>()

    /**
     * Allocate a new [InstancedMap] given [chunks].
     *
     * @param world
     * The [World] that the instanced map is apart of.
     *
     * @param chunks
     * The [InstancedChunkSet] that holds all the [InstancedChunk]s that will make
     * up the newly constructed [InstancedMap], if applicable.
     *
     * @param bypassObjectChunkBounds
     * If true, objects that are found to exceed the bounds of its [Chunk] will
     * not throw an error - however the object will not be applied to the [world]'s
     * [gg.rsmod.game.model.region.ChunkSet], so this flag should be used with
     * that caveat in mind.
     *
     * Explanation:
     * In certain scenarios, an object's tile can overextend its original [Chunk]
     * where it would be placed in the [InstancedMap]; this can occur in any object
     * who's width or length is greater than 1 (one).
     *
     * Example:
     * - 2x2 object is in the local tile of 2,7 (in respect to its [Chunk])
     * - The [InstancedChunk.rot] is set to 2 (two)
     * - The outcome local tile would be 2,-1
     *
     * The outcome local tile would be out-of-bounds in its [Chunk] and would
     * lead to undesired behaviour.
     *
     */
    fun allocate(world: World, chunks: InstancedChunkSet, bypassObjectChunkBounds: Boolean = false): InstancedMap? {
        val area = VALID_AREA
        val step = 64

        /**
         * The total amount of tiles that the new [InstancedMap] will occupy.
         */
        val totalTiles = chunks.regionSize * Chunk.REGION_SIZE

        for (x in area.bottomLeftX until area.topRightX step step) {
            for (z in area.bottomLeftZ until area.topRightZ step step) {

                /**
                 * If a map is already allocated in [x,z], we move on.
                 */
                if (maps.any { it.area.contains(x, z) || it.area.contains(x + totalTiles - 1, z + totalTiles - 1) }) {
                    continue
                }

                val map = allocate(x, z, chunks)
                applyCollision(world, map, bypassObjectChunkBounds)
                maps.add(map)
                return map
            }
        }

        return null
    }

    private fun allocate(x: Int, z: Int, chunks: InstancedChunkSet): InstancedMap =
            InstancedMap(Area(x, z, x + chunks.regionSize * Chunk.REGION_SIZE, z + chunks.regionSize * Chunk.REGION_SIZE), chunks)

    private fun deallocate(world: World, map: InstancedMap) {
        if (maps.remove(map)) {
            removeCollision(world, map)
        }
    }

    fun getMap(tile: Tile): InstancedMap? = maps.find { it.area.contains(tile) }

    fun applyCollision(world: World, map: InstancedMap, bypassObjectChunkBounds: Boolean) {
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

    fun removeCollision(world: World, map: InstancedMap) {
        val regionCount = map.chunks.regionSize
        val chunks = world.chunks

        for (i in 0 until regionCount) {
            val tile = map.area.bottomLeft.transform(i * Chunk.REGION_SIZE, i * Chunk.REGION_SIZE)
            chunks.remove(tile.chunkCoords)
        }
    }
}