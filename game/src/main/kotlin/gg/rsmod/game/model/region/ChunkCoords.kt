package gg.rsmod.game.model.region

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.Tile
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

/**
 * Represents coordinates that can be occupied by a [Chunk].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ChunkCoords(val x: Int, val z: Int) {

    fun toTile(): Tile = Tile((x + 6) shl 3, (z + 6) shl 3)

    fun getSurroundingCoords(chunkRadius: Int = Chunk.CHUNK_VIEW_RADIUS): ObjectOpenHashSet<ChunkCoords> {
        val surrounding = ObjectOpenHashSet<ChunkCoords>()

        for (x in -chunkRadius..chunkRadius) {
            for (z in -chunkRadius..chunkRadius) {
                surrounding.add(ChunkCoords(this.x + x, this.z + z))
            }
        }
        return surrounding
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("x", x).add("z", z).toString()

    override fun equals(other: Any?): Boolean {
        if (other is ChunkCoords) {
            return other.x == x && other.z == z
        }
        return false
    }

    override fun hashCode(): Int = (x shl 16) or z

    companion object {
        fun fromTile(x: Int, z: Int): ChunkCoords = ChunkCoords(x, z)

        fun fromTile(tile: Tile): ChunkCoords = fromTile(tile.topLeftRegionX, tile.topLeftRegionZ)
    }
}