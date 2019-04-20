package gg.rsmod.game.model.collision

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.Direction
import java.util.*
import kotlin.experimental.inv
import kotlin.experimental.or

class CollisionMatrix private constructor(val length: Int, val width: Int, private val matrix: ShortArray) {

    constructor(width: Int, length: Int) : this(length, width, ShortArray(width * length))

    constructor(other: CollisionMatrix) : this(other.length, other.width, other.matrix.copyOf(other.matrix.size))

    fun block(x: Int, y: Int, impenetrable: Boolean = true) {
        set(x, y, if (impenetrable) FULL_COLLISION else ALLOW_PROJECTILE_COLLISION)
    }

    fun set(x: Int, y: Int, flag: CollisionFlag) {
        set(x, y, flag.getBitAsShort())
    }

    fun set(x: Int, y: Int, flag: Short) {
        matrix[indexOf(x, y)] = flag
    }

    fun get(x: Int, y: Int): Int = matrix[indexOf(x, y)].toInt() and 0xFFFF

    fun hasAllFlags(x: Int, y: Int, vararg flags: CollisionFlag): Boolean = flags.all { hasFlag(x, y, it) }

    fun hasAnyFlags(x: Int, y: Int, vararg flags: CollisionFlag): Boolean = flags.any { hasFlag(x, y, it) }

    fun isClipped(x: Int, y: Int): Boolean = CollisionFlag.values.any { hasFlag(x, y, it) }

    fun addFlag(x: Int, y: Int, flag: CollisionFlag) {
        val index = indexOf(x, y)
        matrix[index] = matrix[index] or flag.getBitAsShort()
    }

    fun hasFlag(x: Int, y: Int, flag: CollisionFlag): Boolean = (get(x, y) and flag.getBitAsShort().toInt()) != 0

    fun removeFlag(x: Int, y: Int, flag: CollisionFlag) {
        set(x, y, (matrix[indexOf(x, y)].toInt() and flag.getBitAsShort().inv().toInt()).toShort())
    }

    fun reset() {
        for (x in 0 until width) {
            for (y in 0 until width) {
                reset(x, y)
            }
        }
    }

    fun reset(x: Int, y: Int) {
        set(x, y, NO_COLLISION)
    }

    fun isBlocked(x: Int, y: Int, direction: Direction, projectile: Boolean): Boolean {
        val flags = CollisionFlag.getFlags(projectile)

        val northwest = 0
        val north = 1
        val northeast = 2
        val west = 3
        val east = 4
        val southwest = 5
        val south = 6
        val southeast = 7

        return when (direction) {
            Direction.NORTH_WEST -> hasFlag(x, y, flags[northwest]) || hasFlag(x, y, flags[north]) || hasFlag(x, y, flags[west])
            Direction.NORTH -> hasFlag(x, y, flags[north])
            Direction.NORTH_EAST -> hasFlag(x, y, flags[northeast]) || hasFlag(x, y, flags[north]) || hasFlag(x, y, flags[east])
            Direction.EAST -> hasFlag(x, y, flags[east])
            Direction.SOUTH_EAST -> hasFlag(x, y, flags[southeast]) || hasFlag(x, y, flags[south]) || hasFlag(x, y, flags[east])
            Direction.SOUTH -> hasFlag(x, y, flags[south])
            Direction.SOUTH_WEST -> hasFlag(x, y, flags[southwest]) || hasFlag(x, y, flags[south]) || hasFlag(x, y, flags[west])
            Direction.WEST -> hasFlag(x, y, flags[west])
            else -> throw IllegalArgumentException("Unrecognised direction $direction.")
        }
    }

    private fun indexOf(x: Int, y: Int) = y * width + x

    override fun toString(): String = MoreObjects.toStringHelper(this).add("matrices", Arrays.toString(matrix)).toString()

    companion object {

        private const val NO_COLLISION: Short = 0

        private const val FULL_COLLISION: Short = 65535.toShort()

        private const val ALLOW_PROJECTILE_COLLISION: Short = 65280.toShort()

        fun createMatrices(count: Int, width: Int, length: Int): Array<CollisionMatrix> = Array(count) { CollisionMatrix(width, length) }
    }
}