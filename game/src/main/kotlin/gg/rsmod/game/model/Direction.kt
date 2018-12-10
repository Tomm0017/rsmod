package gg.rsmod.game.model

/**
 * Represents a cardinal and ordinal direction in the game.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class Direction(val value: Int) {

    NONE(-1),

    NORTH_WEST(0),

    NORTH(1),

    NORTH_EAST(2),

    WEST(3),

    EAST(4),

    SOUTH_WEST(5),

    SOUTH(6),

    SOUTH_EAST(7);

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

    companion object {
        val EMPTY_DIRECTION_ARRAY = arrayOfNulls<Direction>(0)

        val NESW = arrayOf(NORTH, EAST, SOUTH, WEST)

        val WNES = arrayOf(WEST, NORTH, EAST, SOUTH)

        val WNES_DIAGONAL = arrayOf(NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST)
    }
}