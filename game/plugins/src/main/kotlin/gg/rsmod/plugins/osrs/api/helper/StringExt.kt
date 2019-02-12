package gg.rsmod.plugins.osrs.api.helper

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun String.plural(amount: Int): String {
    if (endsWith('s')) {
        return this
    }
    return if (amount != 1) this + "s" else this
}