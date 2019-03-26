package gg.rsmod.plugins.api.ext

/**
 * @author Tom <rspsmods@gmail.com>
 */

private const val vowels = "aeiou"

fun String.plural(amount: Int): String {
    if (endsWith('s')) {
        return this
    }
    return if (amount != 1) this + "s" else this
}

/**
 * Prefixes the string with either "a" or "an" depending on whether
 * the string starts with a vowel
 */
fun String.prefixAn() : String {
    return if (vowels.indexOf(Character.toLowerCase(this[0])) != -1) "an $this" else "a $this"
}