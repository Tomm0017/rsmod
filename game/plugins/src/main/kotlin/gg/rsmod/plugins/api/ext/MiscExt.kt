package gg.rsmod.plugins.api.ext

import java.text.DecimalFormat
import java.text.Format

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Number.appendToString(string: String): String = "$this $string" + (if (this != 1) "s" else "")

fun Number.format(format: Format): String = format.format(this)

fun Number.decimalFormat(): String = format(DecimalFormat())

fun String.parseAmount(): Long = when {
    endsWith("k") -> substring(0, length - 1).toLong() * 1000
    endsWith("m") -> substring(0, length - 1).toLong() * 1_000_000
    endsWith("b") -> substring(0, length - 1).toLong() * 1_000_000_000
    else -> substring(0, length).toLong()
}