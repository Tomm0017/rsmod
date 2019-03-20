package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.PlayerUID
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.region.Chunk
import java.util.*

/**
 * A dynamically constructed map area made up of one or more [InstancedChunk]s.
 *
 * @param area
 * The total area that this map covers in the game world.
 *
 * @param chunks
 * The copied chunks that make up this instanced map.
 *
 * @param exitTile
 * The [Tile] that players will be taken to under certain circumstances, such as
 * logging out of the game while in the instanced map area.
 *
 * @param owner
 * The [PlayerUID] of the 'owner'. This value can be null, unless [attr] contains
 * an attribute which requires an owner.
 *
 * @param attr
 * A set of [InstancedMapAttribute]s that our map has.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMap internal constructor(val area: Area, val chunks: InstancedChunkSet, val exitTile: Tile,
                                        val owner: PlayerUID?, val attr: EnumSet<InstancedMapAttribute>) {

    /**
     * Get the [InstancedChunk.packed] values in relation to the [relative] tile.
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