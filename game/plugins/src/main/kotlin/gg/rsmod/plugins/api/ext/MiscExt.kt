package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.game.model.timer.TimerMap
import java.text.DecimalFormat
import java.text.Format
import java.util.*
import java.util.concurrent.ThreadLocalRandom

private val RANDOM = ThreadLocalRandom.current()

fun Number.appendToString(string: String): String = "$this $string" + (if (this != 1) "s" else "")

fun Number.format(format: Format): String = format.format(this)

fun Number.decimalFormat(): String = format(DecimalFormat())

fun String.parseAmount(): Long = when {
    endsWith("k") -> substring(0, length - 1).toLong() * 1000
    endsWith("m") -> substring(0, length - 1).toLong() * 1_000_000
    endsWith("b") -> substring(0, length - 1).toLong() * 1_000_000_000
    else -> substring(0, length).toLong()
}

fun Int.interpolate(minChance: Int, maxChance: Int, minLvl: Int, maxLvl: Int): Int =
        minChance + (maxChance - minChance) * (this - minLvl) / (maxLvl - minLvl)

fun Int.interpolate(minChance: Int, maxChance: Int, minLvl: Int, maxLvl: Int, cap: Int): Boolean =
        RANDOM.nextInt(cap) <= interpolate(minChance, maxChance, minLvl, maxLvl)

/**
 * Get time left from a [TimerKey], in minutes.
 *
 * @return
 * Null if the minutes left is less than 1 (one). Minutes left in timer key otherwise.
 */
fun TimerMap.getMinutesLeft(key: TimerKey): Int? {
    val cyclesLeft = get(key)
    val minutes = (cyclesLeft / 100.0).toInt()
    if (minutes > 0) {
        return minutes
    }
    return null
}

/**
 * Create an empty [EnumSet] of type [T].
 */
inline fun <reified T: Enum<T>> enumSetOf() = EnumSet.noneOf(T::class.java)

/**
 * Create an [EnumSet] made up of [values].
 *
 * @param values the default values stored in our set.
 */
inline fun <reified T: Enum<T>> enumSetOf(vararg values: T) = EnumSet.noneOf(T::class.java).apply { addAll(values) }

/**
 * Get a random tile within the bounds of this area. Does <strong>not</strong>
 * take into account clipped tiles within the area.
 */
val Area.randomTile: Tile get() = Tile(bottomLeftX + RANDOM.nextInt((topRightX - bottomLeftX) + 1), bottomLeftZ + RANDOM.nextInt((topRightZ - bottomLeftZ) + 1))