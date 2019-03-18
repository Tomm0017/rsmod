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
        private val VALID_AREA = Area(6400, 128, 9600, 6400)//Area(bottomLeftX = 6400, bottomLeftZ = 0, topRightX = 9600, topRightZ = 6400)
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

    private fun deallocate(map: InstancedMap) {
        if (maps.remove(map)) {
            removeCollision(map)
        }
    }

    fun applyCollision(world: World, map: InstancedMap, bypassObjectChunkBounds: Boolean) {
        val bounds = InstancedChunkSet.CHUNK_BOUNDS
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

                    val baseTile = map.area.bottomLeft.transform((chunkX - 6) shl 3, (chunkZ - 6) shl 3, chunkH)
                    val newChunk = world.chunks.getOrCreate(baseTile)

                    if (chunk != null) {
                        val copyTile = Tile.fromRotatedHash(chunk.packed)
                        val copyChunk = world.chunks.get(copyTile.chunkCoords, createIfNeeded = true)!!

                        copyChunk.getEntities<StaticObject>(EntityType.STATIC_OBJECT).forEach { obj ->
                            if (obj.tile.height == chunkH && obj.tile.isInSameChunk(copyTile)) {
                                val def = obj.getDef(world.definitions)
                                val width = def.getRotatedWidth(obj)
                                val length = def.getRotatedLength(obj)

                                val localX = obj.tile.x - ((obj.tile.x shr 3) shl 3)
                                val localZ = obj.tile.z - ((obj.tile.z shr 3) shl 3)

                                val newObj = DynamicObject(obj.id, obj.type, (obj.rot + chunk.rot) and 0x3, baseTile.transformAndRotate(localX, localZ, chunk.rot, width, length))

                                if (newObj.tile.isInSameChunk(baseTile)) {
                                    newChunk.addEntity(world, newObj, newObj.tile)

                                } else if (!bypassObjectChunkBounds) {
                                    throw IllegalStateException("Could not copy object due to its size and rotation outcome (object rotation + chunk rotation). " +
                                            "The object would, otherwise, be spawned out of bounds of its original chunk. [obj=$obj, copy=$newObj]")
                                }
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

    fun removeCollision(map: InstancedMap) {

    }
}