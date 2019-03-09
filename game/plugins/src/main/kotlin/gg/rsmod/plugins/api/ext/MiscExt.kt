package gg.rsmod.plugins.api.ext

import java.text.DecimalFormat
import java.text.Format

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Number.appendToString(string: String): String = "$this $string" + (if (this != 1) "s" else "")

fun Number.format(format: Format): String = format.format(this)

fun Number.decimalFormat(): String = format(DecimalFormat())