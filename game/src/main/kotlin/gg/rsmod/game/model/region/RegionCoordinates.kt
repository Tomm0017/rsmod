package gg.rsmod.game.model.region

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.Tile

class RegionCoordinates(val x: Int, val z: Int) {

    companion object {
        fun fromTile(tile: Tile): RegionCoordinates = RegionCoordinates(tile.calculateTopLeftRegionX(), tile.calculateTopLeftRegionZ())
    }

    fun toTile(): Tile = Tile((x + 6) shl 3, (z + 6) shl 3)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("x", x).add("z", z).toString()

    override fun equals(other: Any?): Boolean {
        if (other is RegionCoordinates) {
            return other.x == x && other.z == z
        }
        return false
    }

    override fun hashCode(): Int = (x shl 16) or z
}