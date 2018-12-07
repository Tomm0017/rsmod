package gg.rsmod.game.model

import com.google.common.base.MoreObjects

/**
 * A 3D point in the world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Tile(val x: Int, val z: Int, val height: Int = 0) {

    constructor(other: Tile) : this(other.x, other.z, other.height)

    fun transform(x: Int, z: Int, height: Int) = Tile(this.x + x, this.z + z, this.height + height)

    fun transform(x: Int, z: Int): Tile = Tile(this.x + x, this.z + z, this.height)

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

    /**
     * Checks if the [other] tile has the same coordinates as this tile.
     */
    fun sameCoordinates(other: Tile): Boolean = other.x == x && other.z == z && other.height == height

    /**
     * Get the region id based on these coordinates.
     */
    fun toRegionId(): Int = ((x shr 6) shl 8) or (z shr 6)

    /**
     * The tile presented as a 32-bit integer.
     */
    fun toInteger(): Int = (z and 0x3FFF) or (x and 0x3FFF shl 14) or (height and 0x3 shl 28)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("x", x).add("z", z).add("height", height).toString()
}