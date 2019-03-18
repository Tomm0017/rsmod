package gg.rsmod.game.model

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.ChunkCoords

/**
 * A 3D point in the world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Tile {

    companion object {
        /**
         * The total amount of height levels that can be used in the game.
         */
        const val TOTAL_HEIGHT_LEVELS = 4
    }

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
        check(height < TOTAL_HEIGHT_LEVELS) { "Tile height level should not exceed maximum height! [height=$height]" }
    }

    constructor(x: Int, z: Int, height: Int = 0) : this((x and 0x7FFF) or ((z and 0x7FFF) shl 15) or (height shl 30))

    constructor(other: Tile) : this(other.x, other.z, other.height)

    fun transform(x: Int, z: Int, height: Int) = Tile(this.x + x, this.z + z, this.height + height)

    fun transform(x: Int, z: Int): Tile = Tile(this.x + x, this.z + z, this.height)

    fun transform(height: Int): Tile = Tile(this.x, this.z, this.height + height)

    fun viewableFrom(other: Tile, viewDistance: Int = 15): Boolean = getDistance(other) <= viewDistance

    fun step(direction: Direction, num: Int = 1): Tile = Tile(this.x + (num * direction.getDeltaX()), this.z + (num * direction.getDeltaZ()), this.height)

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

    fun getDistance(other: Tile): Int {
        val dx = x - other.x
        val dz = z - other.z
        return Math.ceil(Math.sqrt((dx * dx + dz * dz).toDouble())).toInt()
    }

    fun getDelta(other: Tile): Int = Math.abs(x - other.x) + Math.abs(z - other.z)

    /**
     * Returns the local tile of our region relative to the current [x] and [z].
     *
     * The [other] tile will always have coords equal to or greater than our own.
     */
    fun toLocal(other: Tile): Tile = Tile(((other.x shr 3) - (x shr 3)) shl 3, ((other.z shr 3) - (z shr 3)) shl 3, height)

    val getTopLeftRegionX: Int get() = (x shr 3) - 6

    val getTopLeftRegionZ: Int get() = (z shr 3) - 6

    /**
     * Get the region id based on these coordinates.
     */
    val regionId: Int get() = ((x shr 6) shl 8) or (z shr 6)

    /**
     * Returns the base tile of our region relative to the current [x], [z] and [Chunk.MAX_VIEWPORT].
     */
    val asRegionBase: Tile get() = Tile(((x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3, ((z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3, height)

    val asChunkCoords: ChunkCoords get() = ChunkCoords.fromTile(this)

    /**
     * The tile packed as a 30-bit integer.
     */
    val as30BitInteger: Int get() = (z and 0x3FFF) or (x and 0x3FFF shl 14) or (height and 0x3 shl 28)

    /**
     * Checks if the [other] tile has the same coordinates as this tile.
     */
    fun sameAs(other: Tile): Boolean = other.x == x && other.z == z && other.height == height

    fun sameAs(x: Int, z: Int): Boolean = x == this.x && z == this.z

    override fun toString(): String = MoreObjects.toStringHelper(this).add("x", x).add("z", z).add("height", height).toString()

    override fun hashCode(): Int = coordinate

    override fun equals(other: Any?): Boolean {
        if (other is Tile) {
            return other.coordinate == coordinate
        }
        return false
    }
}