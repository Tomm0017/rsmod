package gg.rsmod.game.model.region

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import it.unimi.dsi.fastutil.ints.IntOpenHashSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkSet(val world: World) {

    companion object {
        const val DEFAULT_TOTAL_HEIGHTS = 4
    }

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

    private val chunks = hashMapOf<ChunkCoords, Chunk>()

    private val activeRegions = IntOpenHashSet()

    fun getActiveChunks(): Int = chunks.size

    fun getActiveRegions(): Int = activeRegions.size

    fun getOrCreate(tile: Tile): Chunk = get(tile.toChunkCoords(), createIfNeeded = true)!!

    fun get(tile: Tile, create: Boolean = false): Chunk? = get(tile.toChunkCoords(), create)

    fun get(coords: ChunkCoords, createIfNeeded: Boolean = false): Chunk? {
        val chunk = chunks[coords]
        if (chunk != null) {
            return chunk
        } else if (!createIfNeeded) {
            return null
        }
        val regionId = coords.toTile().toRegionId()
        val newChunk = Chunk(coords, DEFAULT_TOTAL_HEIGHTS)

        chunks[coords] = newChunk
        if (activeRegions.add(regionId)) {
            world.definitions.createRegion(world, regionId)
        }
        return newChunk
    }
}