package gg.rsmod.plugins.api.ext

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Number.appendToString(string: String): String = "$this $string" + (if (this != 1) "s" else "")