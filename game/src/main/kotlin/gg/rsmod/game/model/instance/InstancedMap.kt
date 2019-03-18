package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.region.Chunk

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMap(val area: Area, val chunks: InstancedChunkSet) {

    /**
     * Get the [InstancedChunk.packed] values.
     *
     * The values are represented as:
     * `
     * zzzzzzzzzzzzz-xxxxxxxxxxxxx-hhhhhhhhhhhhh
     * zzzzzzzzzzzzz-xxxxxxxxxxxxx-hhhhhhhhhhhhh
     * zzzzzzzzzzzzz-xxxxxxxxxxxxx-hhhhhhhhhhhhh
     * zzzzzzzzzzzzz-xxxxxxxxxxxxx-hhhhhhhhhhhhh
     * `
     */
    fun getCoordinates(relative: Tile): IntArray {
        val heights = Tile.TOTAL_HEIGHT_LEVELS
        val bounds = Chunk.CHUNKS_PER_REGION

        val coordinates = IntArray(heights * bounds * bounds)

        val startX = relative.x - 48
        val startZ = relative.z - 48

        var index = 0
        for (height in 0 until heights) {
            for (x in 0 until bounds) {
                for (z in 0 until bounds) {
                    val absolute = Tile(startX + (x shl 3), startZ + (z shl 3))
                    val chunkX = (absolute.x - area.bottomLeftX) shr 3
                    val chunkZ = (absolute.z - area.bottomLeftZ) shr 3

                    val coord = InstancedChunkSet.getCoordinates(chunkX, chunkZ, height)
                    val chunk = chunks.values[coord]

                    coordinates[index++] = chunk?.packed ?: -1
                }
            }
        }

        return coordinates
    }
}