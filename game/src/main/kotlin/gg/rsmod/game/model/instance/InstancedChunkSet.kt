package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Tile

/**
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

        fun setRegionSize(regionSize: Int): Builder {
            this.regionSize = regionSize
            return this
        }

        fun set(chunkX: Int, chunkZ: Int, height: Int = 0, rotation: Int = 0, copy: Tile): Builder {
            check(height in 0 until Tile.TOTAL_HEIGHT_LEVELS) { "Height must be in bounds [0-3]" }
            check(rotation in 0..3) { "Rotation must be in bounds [0-3]" }

            if (regionSize < (chunkX shr 3) + 1 || regionSize < (chunkZ shr 3) + 1) {
                regionSize = Math.max((chunkX shr 3) + 1, (chunkZ shr 3) + 1)
            }

            val packed = copy.toRotatedInteger(rotation)
            val chunk = InstancedChunk(packed, rotation)

            val coordinates = getCoordinates(chunkX, chunkZ, height)
            chunks[coordinates] = chunk
            return this
        }
    }
}