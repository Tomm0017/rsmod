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

    /**
     * A bit-packed integer that holds and represents the [x], [z] and [height] of the tile.
     */
    private val coordinate: Int

    val x: Int get() = coordinate and 0x7FFF

    val z: Int get() = (coordinate shr 15) and 0x7FFF

    val height: Int get() = coordinate ushr 30

    val topLeftRegionX: Int get() = (x shr 3) - 6

    val topLeftRegionZ: Int get() = (z shr 3) - 6

    /**
     * Get the region id based on these coordinates.
     */
    val regionId: Int get() = ((x shr 6) shl 8) or (z shr 6)

    /**
     * Returns the base tile of our region relative to the current [x], [z] and [Chunk.MAX_VIEWPORT].
     */
    val regionBase: Tile get() = Tile(((x shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3, ((z shr 3) - (Chunk.MAX_VIEWPORT shr 4)) shl 3, height)

    val chunkCoords: ChunkCoords get() = ChunkCoords.fromTile(this)

    /**
     * The tile packed as a 30-bit integer.
     */
    val as30BitInteger: Int get() = (z and 0x3FFF) or ((x and 0x3FFF) shl 14) or ((height and 0x3) shl 28)

    val asTileHashMultiplier: Int get() = (z shr 13) or ((x shr 13) shl 8) or ((height and 0x3) shl 16)

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

    fun transformAndRotate(localX: Int, localZ: Int, orientation: Int, width: Int = 1, length: Int = 1): Tile {
        val localWidth = Chunk.CHUNK_SIZE - 1
        val localLength = Chunk.CHUNK_SIZE - 1

        return when (orientation) {
            0 -> transform(localX, localZ)
            1 -> transform(localZ, localLength - localX - (width - 1))
            2 -> transform(localWidth - localX - (width - 1), localLength - localZ - (length - 1))
            3 -> transform(localWidth - localZ - (length - 1), localX)
            else -> throw IllegalArgumentException("Illegal orientation! Value must be in bounds [0-3]. [orientation=$orientation]")
        }
    }

    /**
     * Checks if the [other] tile is within the [radius]x[radius] distance of
     * this [Tile].
     *
     * @return true
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

    fun isInSameChunk(other: Tile): Boolean = (x shr 3) == (other.x shr 3) && (z shr 3) == (other.z shr 3)

    fun getDistance(other: Tile): Int {
        val dx = x - other.x
        val dz = z - other.z
        return Math.ceil(Math.sqrt((dx * dx + dz * dz).toDouble())).toInt()
    }

    fun getDelta(other: Tile): Int = Math.abs(x - other.x) + Math.abs(z - other.z)

    /**
     * @return
     * The local tile of our region relative to the current [x] and [z].
     *
     * The [other] tile will always have coords equal to or greater than our own.
     */
    fun toLocal(other: Tile): Tile = Tile(((other.x shr 3) - (x shr 3)) shl 3, ((other.z shr 3) - (z shr 3)) shl 3, height)

    /**
     * @return
     * A bit-packed value of the tile, in [Chunk] coordinates, which also stores
     * a rotation/orientation value.
     */
    fun toRotatedInteger(rot: Int): Int = ((height and 0x3) shl 24) or (((x shr 3) and 0x3FF) shl 14) or (((z shr 3) and 0x7FF) shl 3) or ((rot and 0x3) shl 1)

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

    operator fun component1() = x

    operator fun component2() = z

    operator fun component3() = height

    companion object {
        /**
         * The total amount of height levels that can be used in the game.
         */
        const val TOTAL_HEIGHT_LEVELS = 4

        fun fromRotatedHash(packed: Int): Tile {
            val x = ((packed shr 14) and 0x3FF) shl 3
            val z = ((packed shr 3) and 0x7FF) shl 3
            val height = (packed shr 28) and 0x3
            return Tile(x, z, height)
        }

        fun from30BitHash(packed: Int): Tile {
            val x = ((packed shr 14) and 0x3FFF)
            val z = ((packed) and 0x3FFF)
            val height = (packed shr 28)
            return Tile(x, z, height)
        }
    }
}