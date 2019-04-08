package gg.rsmod.game.model.region

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * Stores and exposes [Chunk]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkSet(val world: World) {

    /**
     * Copies the [CollisionMatrix] data from all [Chunk]s that are within
     * the specified [radius] in the height level of [height].
     *
     * @param chunkCoords
     * The centre [ChunkCoords].
     *
     * @param height
     * The height level of which to copy the [CollisionMatrix] data from.
     *
     * @param radius
     * The radius, in which to copy [CollisionMatrix] data from in relation
     * to [chunkCoords], in chunk coordinates.
     */
    fun copyChunksWithinRadius(chunkCoords: ChunkCoords, height: Int, radius: Int): ChunkSet {
        val newSet = ChunkSet(world)
        val surrounding = chunkCoords.getSurroundingCoords(radius)

        surrounding.forEach { coords ->
            val chunk = get(coords, createIfNeeded = true)!!
            val copy = Chunk(coords, chunk.heights)
            copy.setMatrix(height, CollisionMatrix(chunk.getMatrix(height)))
            newSet.chunks[coords] = copy
        }
        return newSet
    }

    private val chunks = Object2ObjectOpenHashMap<ChunkCoords, Chunk>()

    private val activeRegions = IntOpenHashSet()

    fun getActiveChunkCount(): Int = chunks.size

    fun getActiveRegionCount(): Int = activeRegions.size

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param tile
     * The [Tile] to get the [ChunkCoords] from.
     */
    fun getOrCreate(tile: Tile): Chunk = get(tile.chunkCoords, createIfNeeded = true)!!

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param tile
     * The [Tile] to get the [ChunkCoords] from.
     *
     * @param createIfNeeded
     * Create the [Chunk] if it does not already exist in our [chunks].
     */
    fun get(tile: Tile, createIfNeeded: Boolean = false): Chunk? = get(tile.chunkCoords, createIfNeeded)

    /**
     * Get the [Chunk] that corresponds to the given [chunks].
     *
     * @param coords
     * The [ChunkCoords] to get the [Chunk] for.
     *
     * @param createIfNeeded
     * Create the [Chunk] if it does not already exist in our [chunks].
     */
    fun get(coords: ChunkCoords, createIfNeeded: Boolean = false): Chunk? {
        val chunk = chunks[coords]
        if (chunk != null) {
            return chunk
        } else if (!createIfNeeded) {
            return null
        }
        val regionId = coords.toTile().regionId
        val newChunk = Chunk(coords, Tile.TOTAL_HEIGHT_LEVELS)
        newChunk.createEntityContainers()

        chunks[coords] = newChunk
        if (activeRegions.add(regionId)) {
            world.definitions.createRegion(world, regionId)
        }
        return newChunk
    }

    fun remove(coords: ChunkCoords): Boolean = chunks.remove(coords) != null
}