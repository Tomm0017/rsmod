package gg.rsmod.game.model.region

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkSet(val world: World) {

    companion object {
        const val DEFAULT_TOTAL_HEIGHTS = 4
    }

    private val chunks = Object2ObjectOpenHashMap<ChunkCoords, Chunk>()

    private val activeRegions = IntOpenHashSet()

    fun getActiveChunks(): Int = chunks.size

    fun getActiveRegions(): Int = activeRegions.size

    fun getForTile(tile: Tile): Chunk = getOrCreate(ChunkCoords.fromTile(tile), true)!!

    fun getOrCreate(coords: ChunkCoords, create: Boolean = false): Chunk? {
        val chunk = chunks[coords]
        if (chunk != null) {
            return chunk
        } else if (!create) {
            return null
        }
        val newChunk = Chunk(coords, DEFAULT_TOTAL_HEIGHTS)
        val regionId = coords.toTile().toRegionId()
        chunks[coords] = newChunk
        if (activeRegions.add(regionId)) {
            world.definitions.createRegion(world, regionId)
        }
        return newChunk
    }
}