package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Tile

/**
 * A set of [InstancedChunk]s that can be used to construct an [InstancedMap].
 *
 * @param regionSize
 * The amount of regions (64x64 tiles) that the [values] occupy.
 *
 * @param values
 * A map of [InstancedChunk]s that this set makes up, with the chunk coordinates,
 * in relation to map, as a key.
 *
 * For example
 * The most bottom-left chunk would have a chunk coordinate of 0,0.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedChunkSet(val regionSize: Int, val values: Map<Int, InstancedChunk>) {

    companion object {
        fun getCoordinates(x: Int, z: Int, height: Int): Int = ((height and 0x3) shl 28) or ((x and 0x3FF) shl 14) or (z and 0x7FF)
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

        fun set(chunkX: Int, chunkZ: Int, height: Int = 0, rot: Int = 0, copy: Tile): Builder {
            check(height in 0 until Tile.TOTAL_HEIGHT_LEVELS) { "Height must be in bounds [0-3]" }
            check(rot in 0..3) { "Rotation must be in bounds [0-3]" }

            if (regionSize < (chunkX shr 3) + 1 || regionSize < (chunkZ shr 3) + 1) {
                regionSize = Math.max((chunkX shr 3) + 1, (chunkZ shr 3) + 1)
            }

            val packed = copy.toRotatedInteger(rot)
            val chunk = InstancedChunk(packed)

            val coordinates = getCoordinates(chunkX, chunkZ, height)
            chunks[coordinates] = chunk
            return this
        }
    }
}