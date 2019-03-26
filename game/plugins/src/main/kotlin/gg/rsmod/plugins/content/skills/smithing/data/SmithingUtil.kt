package gg.rsmod.plugins.content.skills.smithing.data

/**
 * Gets the bar id for a specified item name. I wasn't able to find any reference
 * to bars in the cache enums or item definitions, so I figured this would at least
 * achieve the desired affect.
 *
 * @param name  The name of the item
 * @return      The bar id
 */
internal fun getBar(name: String) : Bar? {
    return Bar.values.firstOrNull { name.startsWith(it.prefix) } ?: when {
        name.endsWith("lantern frame") -> Bar.IRON
        name.endsWith("lantern (unf)") -> Bar.STEEL
        name.endsWith("grapple tip") -> Bar.MITHRIL
        else -> null
    }
}

/**
 * Gets the item 'type' for a specified child id
 *
 * @param child The child id
 * @param bar   The bar type
 * @return      The type, as a string
 */
internal fun typeForChild(child: Int, bar: Bar) : String? = when (child) {
    2 -> "${bar.prefix} dagger"
    3 -> "${bar.prefix} sword"
    4 -> "${bar.prefix} scimitar"
    5 -> "${bar.prefix} longsword"
    6 -> "${bar.prefix} 2h sword"
    7 -> "${bar.prefix} axe"
    8 -> "${bar.prefix} mace"
    9 -> "${bar.prefix} warhammer"
    10 -> "${bar.prefix} battleaxe"
    11 -> "${bar.prefix} claws"
    12 -> "${bar.prefix} chainbody"
    13 -> "${bar.prefix} platelegs"
    14 -> "${bar.prefix} plateskirt"
    15 -> "${bar.prefix} platebody"
    16 -> "${bar.prefix} nails"
    17 -> "${bar.prefix} med helm"
    18 -> "${bar.prefix} full helm"
    19 -> "${bar.prefix} sq shield"
    20 -> "${bar.prefix} kiteshield"
    21 -> when (bar) {
        Bar.IRON -> "lantern frame"
        Bar.STEEL -> "lantern (unf)"
        else -> null
    }
    22 -> "" // Shayzien platebody 4
    23 -> "${bar.prefix} dart tip"
    24 -> "${bar.prefix} arrowtips"
    25 -> "${bar.prefix} knife"
    26 -> when (bar) {
        Bar.BRONZE -> "wire"
        Bar.IRON -> "spit"
        Bar.STEEL -> "studs"
        Bar.MITHRIL -> "grapple tip"
        else -> null
    }
    27 -> "" // Shayzien platebody 5
    28 -> "${bar.prefix} bolts (unf)"
    29 -> "${bar.prefix} limbs"
    30 -> "${bar.prefix} javelin heads"
    else -> null
}