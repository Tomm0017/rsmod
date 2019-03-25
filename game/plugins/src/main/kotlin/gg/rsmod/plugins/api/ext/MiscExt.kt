package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.Tile
import java.text.DecimalFormat
import java.text.Format
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Tom <rspsmods@gmail.com>
 */

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

val Area.randomTile: Tile get() = Tile(bottomLeftX + RANDOM.nextInt((topRightX - bottomLeftX) + 1), bottomLeftZ + RANDOM.nextInt((topRightZ - bottomLeftZ) + 1))