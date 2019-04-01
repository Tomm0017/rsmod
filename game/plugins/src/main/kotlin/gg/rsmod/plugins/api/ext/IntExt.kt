package gg.rsmod.plugins.api.ext

/**
 * @author Triston Plummer ("Dread")
 */

/**
 * A map of numbers to English string literals
 */
private val numberLiterals = hashMapOf(
        1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six",
        7 to "seven", 8 to "eight", 9 to "nine", 10 to "ten", 11 to "eleven", 12 to "twelve",
        13 to "thirteen", 14 to "fourteen", 15 to "fifteen", 16 to "sixteen", 17 to "seventeen",
        18 to "eighteen", 19 to "nineteen", 20 to "twenty", 30 to "thirty", 40 to "forty", 50 to "fifty",
        60 to "sixty", 70 to "seventy", 80 to "eighty", 90 to "ninety"
)

/**
 * Gets the string literal for an integer. If a literal cannot be found, this function
 * will attempt to generate the literal, and cache the result. Only supports up to 99.
 *
 * Example: 73.toLiteral() will produce the value "seventy-three"
 *
 * @return      The string literal
 */
fun Int.toLiteral() : String? {
    var literal = numberLiterals[this]

    if (literal == null && this > 20 && this < 100) {
        val single = (this % 10)
        val ten = (this - single)

        literal = "${numberLiterals[ten]}-${numberLiterals[single]}"
        numberLiterals[this] = literal
    }

    return literal
}

/**
 * Formats a quantity to an amount as it would appear in RuneScape.
 *
 * Examples:
 *
 * 100_000 would return 100K
 * 1_306_000 would return 1306K
 * 13_060_000 would return 13M
 */
fun Int.formatRS2() : String {
    return when {
        this == Integer.MIN_VALUE -> "-${formatRS2()}"
        this < 0 -> "-${(-this).formatRS2()}"
        this < 100_000 -> "$this"
        this < 10_000_000 -> "${this / 1_000}K"
        else -> "${this / 1_000_000}M"
    }
}