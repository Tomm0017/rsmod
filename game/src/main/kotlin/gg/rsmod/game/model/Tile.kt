package gg.rsmod.game.model

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.region.RegionCoordinates

/**
 * A 3D point in the world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Tile {

    /**
     * A bit-packed integer that holds and represents the [x], [z] and [height] of the tile.
     */
    private val coordinate: Int

    val x: Int
        get() = coordinate and 0x7FFF

    val z: Int
        get() = (coordinate shr 15) and 0x7FFF

    val height: Int
        get() = coordinate ushr 30

    private constructor(coordinate: Int) {
        this.coordinate = coordinate
    }

    constructor(x: Int, z: Int, height: Int = 0) : this((x and 0x7FFF) or ((z and 0x7FFF) shl 15) or (height shl 30))

    constructor(other: Tile) : this(other.x, other.z, other.height)

    fun transform(x: Int, z: Int, height: Int) = Tile(this.x + x, this.z + z, this.height + height)

    fun transform(x: Int, z: Int): Tile = Tile(this.x + x, this.z + z, this.height)

    fun step(num: Int, direction: Direction): Tile = Tile(this.x + (num * direction.getDeltaX()), this.z + (num * direction.getDeltaZ()), this.height)

    /**
     * Checks if the [other] tile is within the [radius]x[radius] distance of
     * this [Tile].
     *
     * @return [true]
     * if the tiles are on the same height and within radius of [radius] tiles.
     */
    fun isWithinRadius(other: Tile, radius: Int): Boolean {
        if (other.height != height) {
            return false
        }
        val dx = Math.abs(x - other.x)
        val dz = Math.abs(z - other.z)
        return dx <= radius && dz <= radius
    }

    fun calculateDistance(other: Tile): Int = Math.sqrt((other.x - x * other.x - x + other.z - z * other.z - z).toDouble()).toInt()

    fun calculateTopLeftRegionX() = (x shr 3) - 6

    fun calculateTopLeftRegionZ() = (z shr 3) - 6

    /**
     * Checks if the [other] tile has the same coordinates as this tile.
     */
    fun sameAs(other: Tile): Boolean = other.x == x && other.z == z && other.height == height

    fun sameAs(x: Int, z: Int): Boolean = x == this.x && z == this.z

    fun toRegionCoordinates(): RegionCoordinates = RegionCoordinates.fromTile(this)

    /**
     * Get the region id based on these coordinates.
     */
    fun toRegionId(): Int = ((x shr 6) shl 8) or (z shr 6)

    /**
     * The tile packed as a 30-bit integer.
     */
    fun to30BitInteger(): Int = (z and 0x3FFF) or (x and 0x3FFF shl 14) or (height and 0x3 shl 28)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("x", x).add("z", z).add("height", height).toString()

    override fun hashCode(): Int = coordinate

    override fun equals(other: Any?): Boolean {
        if (other is Tile) {
            return other.coordinate == coordinate
        }
        return false
    }
}