package gg.rsmod.game.model

/**
 * Represents cardinal and ordinal directions in the game.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class Direction(val orientationValue: Int, val playerWalkValue: Int, val npcWalkValue: Int) {

    NONE(orientationValue = -1, playerWalkValue = -1, npcWalkValue = -1),

    NORTH_WEST(orientationValue = 0, playerWalkValue = 5, npcWalkValue = 0),

    NORTH(orientationValue = 1, playerWalkValue = 6, npcWalkValue = 1),

    NORTH_EAST(orientationValue = 2, playerWalkValue = 7, npcWalkValue = 2),

    WEST(orientationValue = 3, playerWalkValue = 3, npcWalkValue = 3),

    EAST(orientationValue = 4, playerWalkValue = 4, npcWalkValue = 4),

    SOUTH_WEST(orientationValue = 5, playerWalkValue = 0, npcWalkValue = 5),

    SOUTH(orientationValue = 6, playerWalkValue = 1, npcWalkValue = 6),

    SOUTH_EAST(orientationValue = 7, playerWalkValue = 2, npcWalkValue = 7);

    fun isDiagonal(): Boolean = this == SOUTH_EAST || this == SOUTH_WEST || this == NORTH_EAST || this == NORTH_WEST

    fun getDeltaX(): Int = when (this) {
        SOUTH_EAST, NORTH_EAST, EAST -> 1
        SOUTH_WEST, NORTH_WEST, WEST -> -1
        else -> 0
    }

    fun getDeltaZ(): Int = when (this) {
        NORTH_WEST, NORTH_EAST, NORTH -> 1
        SOUTH_WEST, SOUTH_EAST, SOUTH -> -1
        else -> 0
    }

    fun getOpposite(): Direction = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
        NORTH_WEST -> SOUTH_EAST
        NORTH_EAST -> SOUTH_WEST
        SOUTH_EAST -> NORTH_WEST
        SOUTH_WEST -> NORTH_EAST
        else -> NONE
    }

    fun getDiagonalComponents(): Array<Direction> = when (this) {
        NORTH_EAST -> arrayOf(NORTH, EAST)
        NORTH_WEST -> arrayOf(NORTH, WEST)
        SOUTH_EAST -> arrayOf(SOUTH, EAST)
        SOUTH_WEST -> arrayOf(SOUTH, WEST)
        else -> throw IllegalArgumentException("Must provide a diagonal direction.")
    }

    val angle: Int
        get() {
            return when (this) {
                SOUTH -> 0
                SOUTH_WEST -> 256
                WEST -> 512
                NORTH_WEST -> 768
                NORTH -> 1024
                NORTH_EAST -> 1280
                EAST -> 1536
                SOUTH_EAST -> 1792
                NONE -> throw IllegalStateException("Invalid direction: $this")
            }
        }

    companion object {

        val NESW = arrayOf(NORTH, EAST, SOUTH, WEST)

        val WNES = arrayOf(WEST, NORTH, EAST, SOUTH)

        val WNES_DIAGONAL = arrayOf(NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST)

        val RS_ORDER = arrayOf(WEST, EAST, NORTH, SOUTH, SOUTH_WEST, SOUTH_EAST, NORTH_WEST, NORTH_EAST)

        val ANGLED_ORDER = arrayOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)

        fun getForAngle(angle: Int): Direction = ANGLED_ORDER[angle / 45]

        fun between(current: Tile, next: Tile): Direction {
            val deltaX = next.x - current.x
            val deltaZ = next.z - current.z

            return fromDeltas(deltaX, deltaZ)
        }

        private fun fromDeltas(deltaX: Int, deltaZ: Int): Direction {
            when (deltaZ) {
                1 -> when (deltaX) {
                    1 -> return NORTH_EAST
                    0 -> return NORTH
                    -1 -> return NORTH_WEST
                }
                -1 -> when (deltaX) {
                    1 -> return SOUTH_EAST
                    0 -> return SOUTH
                    -1 -> return SOUTH_WEST
                }
                0 -> when (deltaX) {
                    1 -> return EAST
                    0 -> return NONE
                    -1 -> return WEST
                }
            }
            throw IllegalArgumentException("Unhandled delta difference. [$deltaX, $deltaZ]")
        }
    }
}