package gg.rsmod.game.model.region

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RegionSet {

    companion object {
        const val DEFAULT_TOTAL_HEIGHTS = 4
    }

    private val chunks = hashMapOf<RegionCoordinates, Chunk>()

    private val decodedRegions = hashSetOf<Int>()

    fun getChunkForTile(world: World, tile: Tile): Chunk = getChunk(world, RegionCoordinates.fromTile(tile))

    fun getChunk(world: World, coordinates: RegionCoordinates): Chunk {
        val chunk = chunks[coordinates]
        if (chunk != null) {
            return chunk
        }
        val newChunk = Chunk(coordinates, DEFAULT_TOTAL_HEIGHTS)
        val regionId = coordinates.toTile().toRegionId()
        chunks[coordinates] = newChunk
        if (decodedRegions.add(regionId)) {
            world.definitions.createRegion(world, regionId)
        }
        return newChunk
    }
}