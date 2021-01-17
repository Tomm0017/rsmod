package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef

private const val vowels = "aeiou"

fun String.pluralPrefix(amount: Int) : String {
    return if (amount > 1) "are $this" else "is $this"
}

fun String.pluralSuffix(amount: Int): String {
    if (endsWith('s', ignoreCase = true)) {
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

/**
 * very specific string operation to replace "#ITEM" in a string with the [ItemDef.name]
 * of the supplied [Item.id] as found in the supplied [DefinitionSet]
 *
 * @param item - [Item.id] of the [Item] to lookup
 * @param definitions - [DefinitionSet] to search for [ItemDef.name] against
 */
fun String.replaceItemName(item: Int, definitions: DefinitionSet, lowercase: Boolean = true): String {
    return this.replace("#ITEM", item.getItemName(definitions, lowercase))
}

/**
 * very specific string operation to replace "#OBJ" in a string with the [ObjectDef.name]
 * of the supplied [GameObject.id] as found in the supplied [DefinitionSet]
 *
 * @param obj - [GameObject.id] of the [GameObject] to lookup
 * @param definitions - [DefinitionSet] to search for [ObjectDef.name] against
 */
fun String.replaceObjName(obj: Int, definitions: DefinitionSet, lowercase: Boolean = true): String {
    return this.replace("#OBJ", obj.getObjName(definitions, lowercase))
}