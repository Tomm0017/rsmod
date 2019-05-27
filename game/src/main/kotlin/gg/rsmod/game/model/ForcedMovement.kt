package gg.rsmod.game.model

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ForcedMovement internal constructor(private val initialTile: Tile, private val destinations: Array<Tile>,
                                               internal val clientDuration1: Int, internal val clientDuration2: Int,
                                               internal val directionAngle: Int) {

    val diffX1: Int
        get() {
            val dst = destinations[0]
            return dst.x - initialTile.x
        }

    val diffZ1: Int
        get() {
            val dst = destinations[0]
            return dst.z - initialTile.z
        }

    val diffX2: Int
        get() {
            if (destinations.size >= 2) {
                val dst = destinations[1]
                return dst.x - initialTile.x
            }
            return 0
        }

    val diffZ2: Int
        get() {
            if (destinations.size >= 2) {
                val dst = destinations[1]
                return dst.z - initialTile.z
            }
            return 0
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ForcedMovement

        if (initialTile != other.initialTile) return false
        if (!destinations.contentEquals(other.destinations)) return false
        if (clientDuration1 != other.clientDuration1) return false
        if (clientDuration2 != other.clientDuration2) return false
        if (directionAngle != other.directionAngle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initialTile.hashCode()
        result = 31 * result + destinations.contentHashCode()
        result = 31 * result + clientDuration1
        result = 31 * result + clientDuration2
        result = 31 * result + directionAngle
        return result
    }

    companion object {

        fun of(src: Tile, dst: Tile, clientDuration1: Int, directionAngle: Int): ForcedMovement {
            return ForcedMovement(src, arrayOf(dst), clientDuration1, 0, directionAngle)
        }

        fun of(src: Tile, dst1: Tile, dst2: Tile, clientDuration1: Int, clientDuration2: Int, directionAngle: Int): ForcedMovement {
            return ForcedMovement(src, arrayOf(dst1, dst2), clientDuration1, clientDuration2, directionAngle)
        }
    }
}