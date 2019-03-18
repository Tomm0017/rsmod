package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedChunkSet(val regionSize: Int, val values: Map<Int, InstancedChunk>) {

    companion object {

        /**
         * The amount of chunks that fit in either the x or y direction, within
         * a single [InstancedChunkSet].
         */
        const val CHUNK_BOUNDS = 13

        private fun getCoordinates(x: Int, z: Int, height: Int): Int = ((height and 0x3) shl 28) or ((x and 0x3FF) shl 14) or (z and 0x7FF)
    }

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
    fun getCoordinates(): IntArray {
        val heights = Tile.TOTAL_HEIGHT_LEVELS
        val bounds = CHUNK_BOUNDS

        val coordinates = IntArray(heights * bounds * bounds)

        var index = 0
        for (height in 0 until heights) {
            for (x in 0 until bounds) {
                for (z in 0 until bounds) {
                    val coord = getCoordinates(x, z, height)
                    val chunk = values[coord]

                    coordinates[index++] = chunk?.packed ?: -1
                }
            }
        }

        return coordinates
    }

    class Builder {

        private var regionSize = -1

        private val chunks = hashMapOf<Int, InstancedChunk>()

        fun build(): InstancedChunkSet {
            if (regionSize == -1) {
                regionSize = 1
            }
            return InstancedChunkSet(regionSize, chunks)
        }

        fun setRegionSize(regionSize: Int): Builder {
            this.regionSize = regionSize
            return this
        }

        fun set(chunkX: Int, chunkZ: Int, height: Int = 0, rotation: Int = 0, copy: Tile): Builder {
            check(chunkX >= 0) { "ChunkX must be positive." }
            check(chunkZ >= 0) { "ChunkZ must be positive." }
            check(height in 0..Tile.TOTAL_HEIGHT_LEVELS) { "Height must be in bounds [0-3]" }
            check(rotation in 0..3) { "Rotation must be in bounds [0-3]" }

            val packed = copy.toRotatedInteger(rotation)
            val chunk = InstancedChunk(packed, rotation)

            val coordinates = getCoordinates(chunkX + 6, chunkZ + 6, height)
            chunks[coordinates] = chunk
            return this
        }
    }
}